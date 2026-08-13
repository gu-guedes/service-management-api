package com.example.service_management.features.whatsappbot.controller;

import com.example.service_management.features.whatsappbot.service.WhatsAppInboundService;
import tools.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook/whatsapp")
public class WhatsAppWebhookController {

    private final WhatsAppInboundService whatsAppInboundService;

    @Value("${app.whatsapp.verify-token}")
    private String verifyToken;

    public WhatsAppWebhookController(WhatsAppInboundService whatsAppInboundService) {
        this.whatsAppInboundService = whatsAppInboundService;
    }

    @GetMapping
    public ResponseEntity<String> verify(
            @RequestParam(name = "hub.mode", required = false) String mode,
            @RequestParam(name = "hub.verify_token", required = false) String token,
            @RequestParam(name = "hub.challenge", required = false) String challenge) {

        if ("subscribe".equals(mode) && verifyToken != null && verifyToken.equals(token)) {
            return ResponseEntity.ok(challenge);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping
    public ResponseEntity<Void> receive(@RequestBody JsonNode payload) {
        whatsAppInboundService.handleWebhookPayload(payload);
        return ResponseEntity.ok().build();
    }
}
