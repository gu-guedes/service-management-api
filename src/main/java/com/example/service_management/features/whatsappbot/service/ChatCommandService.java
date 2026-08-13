package com.example.service_management.features.whatsappbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ChatCommandService {

    private static final Logger log = LoggerFactory.getLogger(ChatCommandService.class);

    private final FixedSyntaxCommandParser fixedSyntaxCommandParser;
    private final ChatQueryService chatQueryService;
    private final OpenAiClientService openAiClientService;

    public ChatCommandService(FixedSyntaxCommandParser fixedSyntaxCommandParser,
                               ChatQueryService chatQueryService,
                               OpenAiClientService openAiClientService) {
        this.fixedSyntaxCommandParser = fixedSyntaxCommandParser;
        this.chatQueryService = chatQueryService;
        this.openAiClientService = openAiClientService;
    }

    public String handle(String rawMessage) {
        ChatCommand fixed = fixedSyntaxCommandParser.parse(rawMessage);
        if (fixed.intent() != ChatIntent.UNKNOWN) {
            return chatQueryService.execute(fixed);
        }

        // sintaxe fixa nao reconheceu — tenta responder via IA com tool calling
        // (unico ponto que gera custo de token; comando fixo nunca chega aqui)
        try {
            return openAiClientService.answerFreeText(rawMessage);
        } catch (Exception e) {
            log.error("Falha ao responder via IA para mensagem: {}", rawMessage, e);
            return chatQueryService.execute(new ChatCommand(ChatIntent.UNKNOWN, null));
        }
    }
}
