package com.example.service_management.features.whatsappbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OpenAiClientService {

    private static final Logger log = LoggerFactory.getLogger(OpenAiClientService.class);

    private static final int MAX_TOOL_ITERATIONS = 3;

    private static final String SYSTEM_PROMPT_TOOLS = """
            Voce e um assistente de um veterinario, respondendo por WhatsApp em portugues.
            Use as ferramentas disponiveis pra buscar os dados necessarios antes de responder.
            Baseie a resposta somente no resultado das ferramentas — nunca invente um dado que nao veio de uma chamada.
            Se a ferramenta retornar erro (ex: pet nao encontrado), informe isso ao veterinario de forma direta.
            Seja conciso.
            """;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ChatToolExecutor chatToolExecutor;

    @Value("${app.ai.api-key}")
    private String apiKey;

    @Value("${app.ai.transcription-model}")
    private String transcriptionModel;

    @Value("${app.ai.chat-model}")
    private String chatModel;

    public OpenAiClientService(RestTemplate restTemplate, ObjectMapper objectMapper, ChatToolExecutor chatToolExecutor) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.chatToolExecutor = chatToolExecutor;
    }

    public String transcribeAudio(byte[] audioBytes, String mimeType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ByteArrayResource audioResource = new ByteArrayResource(audioBytes) {
            @Override
            public String getFilename() {
                return "audio.ogg";
            }
        };

        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file", audioResource);
        form.add("model", transcriptionModel);
        form.add("language", "pt");

        String response = restTemplate.postForObject(
                "https://api.openai.com/v1/audio/transcriptions",
                new HttpEntity<>(form, headers),
                String.class);

        JsonNode json = objectMapper.readTree(response);
        String text = json.path("text").asText(null);

        if (text == null || text.isBlank()) {
            throw new IllegalStateException("Transcricao vazia recebida da OpenAI: " + response);
        }

        return text;
    }

    // responde uma frase livre deixando a IA decidir quais ferramentas chamar (tool calling) —
    // no maximo MAX_TOOL_ITERATIONS idas-e-voltas, protegendo contra a IA entrar num loop
    public String answerFreeText(String freeText) {
        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", SYSTEM_PROMPT_TOOLS));
        messages.add(Map.of("role", "user", "content", freeText));

        for (int i = 0; i < MAX_TOOL_ITERATIONS; i++) {
            JsonNode message = callChatCompletions(messages);
            JsonNode toolCalls = message.path("tool_calls");

            if (!toolCalls.isArray() || toolCalls.isEmpty()) {
                return message.path("content").asText("Nao consegui responder.");
            }

            messages.add(objectMapper.convertValue(message, Map.class));

            for (JsonNode call : toolCalls) {
                String toolName = call.path("function").path("name").asText();
                JsonNode args = objectMapper.readTree(call.path("function").path("arguments").asText("{}"));
                Map<String, Object> result = chatToolExecutor.execute(toolName, args);

                Map<String, Object> toolMessage = new java.util.HashMap<>();
                toolMessage.put("role", "tool");
                toolMessage.put("tool_call_id", call.path("id").asText());
                toolMessage.put("content", objectMapper.writeValueAsString(result));
                messages.add(toolMessage);
            }
        }

        log.warn("Limite de iteracoes de tool calling atingido para mensagem: {}", freeText);
        return "Nao consegui responder agora. Tente reformular a pergunta.";
    }

    private JsonNode callChatCompletions(List<Map<String, Object>> messages) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "model", chatModel,
                "temperature", 0,
                "messages", messages,
                "tools", ChatToolDefinitions.definitions()
        );

        String response = restTemplate.postForObject(
                "https://api.openai.com/v1/chat/completions",
                new HttpEntity<>(body, headers),
                String.class);

        JsonNode root = objectMapper.readTree(response);
        JsonNode message = root.path("choices").path(0).path("message");

        if (message.isMissingNode()) {
            throw new IllegalStateException("Resposta da OpenAI sem mensagem: " + response);
        }

        return message;
    }
}
