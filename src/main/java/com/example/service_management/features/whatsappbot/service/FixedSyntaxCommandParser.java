package com.example.service_management.features.whatsappbot.service;

import org.springframework.stereotype.Component;

@Component
public class FixedSyntaxCommandParser {

    public ChatCommand parse(String rawMessage) {
        String message = rawMessage == null ? "" : rawMessage.trim();
        String lower = message.toLowerCase();

        if (lower.startsWith("/historico")) {
            String petName = message.substring("/historico".length()).trim();
            return new ChatCommand(ChatIntent.HISTORICO, petName);
        }

        if (lower.startsWith("/quantidade cachorros mes")) {
            return new ChatCommand(ChatIntent.QUANTIDADE_CACHORROS_MES, null);
        }

        if (lower.startsWith("/quantidade gatos mes")) {
            return new ChatCommand(ChatIntent.QUANTIDADE_GATOS_MES, null);
        }

        if (lower.startsWith("/quantidade atendimentos mes")) {
            return new ChatCommand(ChatIntent.QUANTIDADE_ATENDIMENTOS_MES, null);
        }

        if (lower.startsWith("/quantidade retornos pendentes")) {
            return new ChatCommand(ChatIntent.QUANTIDADE_RETORNOS_PENDENTES, null);
        }

        return new ChatCommand(ChatIntent.UNKNOWN, null);
    }
}