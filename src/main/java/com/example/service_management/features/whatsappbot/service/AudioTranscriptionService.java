package com.example.service_management.features.whatsappbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AudioTranscriptionService {

    private static final Logger log = LoggerFactory.getLogger(AudioTranscriptionService.class);

    private final WhatsAppClientService whatsAppClientService;
    private final OpenAiClientService openAiClientService;

    public AudioTranscriptionService(WhatsAppClientService whatsAppClientService, OpenAiClientService openAiClientService) {
        this.whatsAppClientService = whatsAppClientService;
        this.openAiClientService = openAiClientService;
    }

    public Optional<String> transcribeIncomingAudio(String mediaId) {
        try {
            WhatsAppMedia media = whatsAppClientService.downloadMedia(mediaId);
            String transcript = openAiClientService.transcribeAudio(media.content(), media.mimeType());
            return (transcript == null || transcript.isBlank()) ? Optional.empty() : Optional.of(transcript);
        } catch (Exception e) {
            log.error("Falha ao transcrever audio recebido (mediaId={})", mediaId, e);
            return Optional.empty();
        }
    }
}
