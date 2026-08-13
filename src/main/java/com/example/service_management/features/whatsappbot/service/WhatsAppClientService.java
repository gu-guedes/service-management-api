package com.example.service_management.features.whatsappbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Service
public class WhatsAppClientService {

    private static final Logger log = LoggerFactory.getLogger(WhatsAppClientService.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.whatsapp.access-token}")
    private String accessToken;

    @Value("${app.whatsapp.phone-number-id}")
    private String phoneNumberId;

    public WhatsAppClientService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // baixa uma midia recebida (ex: audio) — duas chamadas: metadados (url assinada + mime type)
    // e depois o conteudo em si, ambas autenticadas com o mesmo token do WhatsApp
    public WhatsAppMedia downloadMedia(String mediaId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        String metadataUrl = "https://graph.facebook.com/v25.0/" + mediaId;
        ResponseEntity<String> metadataResponse = restTemplate.exchange(
                metadataUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        JsonNode metadata = objectMapper.readTree(metadataResponse.getBody());
        String mediaUrl = metadata.path("url").asText(null);
        String mimeType = metadata.path("mime_type").asText(null);

        if (mediaUrl == null) {
            throw new IllegalStateException("Resposta de metadados de midia sem 'url': " + metadataResponse.getBody());
        }

        ResponseEntity<byte[]> contentResponse = restTemplate.exchange(
                mediaUrl, HttpMethod.GET, new HttpEntity<>(headers), byte[].class);

        return new WhatsAppMedia(contentResponse.getBody(), mimeType);
    }

    public void sendTextMessage(String to, String body) {
        String url = "https://graph.facebook.com/v25.0/" + phoneNumberId + "/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> payload = Map.of(
                "messaging_product", "whatsapp",
                "to", to,
                "type", "text",
                "text", Map.of("body", body)
        );

        try {
            restTemplate.postForEntity(url, new HttpEntity<>(payload, headers), String.class);
        } catch (Exception e) {
            log.error("Falha ao enviar mensagem WhatsApp pra {}", to, e);
        }
    }
}
