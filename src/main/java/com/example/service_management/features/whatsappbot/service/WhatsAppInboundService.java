package com.example.service_management.features.whatsappbot.service;

import tools.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppInboundService {

    private static final Logger log = LoggerFactory.getLogger(WhatsAppInboundService.class);

    private static final String AUDIO_ERROR_MESSAGE = "Nao consegui entender o audio. Tente novamente ou digite sua pergunta.";
    private static final String UNSUPPORTED_TYPE_MESSAGE = "So consigo processar mensagens de texto ou audio.";

    private final ChatCommandService chatCommandService;
    private final WhatsAppClientService whatsAppClientService;
    private final AudioTranscriptionService audioTranscriptionService;

    @Value("${app.whatsapp.authorized-phone}")
    private String authorizedPhone;

    public WhatsAppInboundService(ChatCommandService chatCommandService,
                                   WhatsAppClientService whatsAppClientService,
                                   AudioTranscriptionService audioTranscriptionService) {
        this.chatCommandService = chatCommandService;
        this.whatsAppClientService = whatsAppClientService;
        this.audioTranscriptionService = audioTranscriptionService;
    }

    public void handleWebhookPayload(JsonNode payload) {
        JsonNode messages = payload
                .path("entry").path(0)
                .path("changes").path(0)
                .path("value").path("messages");

        if (!messages.isArray() || messages.isEmpty()) {
            return;
        }

        JsonNode message = messages.path(0);
        String from = message.path("from").asText(null);
        String type = message.path("type").asText(null);

        if (from == null || type == null) {
            return;
        }

        if (!isAuthorized(from)) {
            log.warn("Mensagem WhatsApp ignorada de numero nao autorizado: {}", from);
            return;
        }

        String reply = switch (type) {
            case "text" -> chatCommandService.handle(message.path("text").path("body").asText(""));
            case "audio" -> audioTranscriptionService.transcribeIncomingAudio(message.path("audio").path("id").asText(null))
                    .map(chatCommandService::handle)
                    .orElse(AUDIO_ERROR_MESSAGE);
            default -> UNSUPPORTED_TYPE_MESSAGE;
        };

        whatsAppClientService.sendTextMessage(from, reply);
    }

    private boolean isAuthorized(String from) {
        if (authorizedPhone == null || authorizedPhone.isBlank()) {
            return false;
        }
        return normalize(from).equals(normalize(authorizedPhone));
    }

    private String normalize(String phone) {
        return phone.replaceAll("[^0-9]", "");
    }
}
