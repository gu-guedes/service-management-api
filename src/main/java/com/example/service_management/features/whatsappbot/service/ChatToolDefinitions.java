package com.example.service_management.features.whatsappbot.service;

import java.util.List;
import java.util.Map;

// definicoes das ferramentas expostas a IA (formato "tools" da API da OpenAI) —
// poucas, mas abrangentes (ex: um so "buscar_dados_pet" cobre idade/peso/raca/etc,
// em vez de uma ferramenta por campo), pra manter o prompt pequeno
public final class ChatToolDefinitions {

    private ChatToolDefinitions() {
    }

    public static List<Map<String, Object>> definitions() {
        return List.of(
                tool("buscar_dados_pet",
                        "Busca o cadastro completo de um pet pelo nome (especie, raca, sexo, idade, peso, observacoes, tutor).",
                        Map.of("nome", Map.of("type", "string", "description", "Nome do pet")),
                        List.of("nome")),
                tool("buscar_historico_atendimentos",
                        "Busca os atendimentos recentes (data, queixa, tratamento) de um pet pelo nome.",
                        Map.of("nome", Map.of("type", "string", "description", "Nome do pet")),
                        List.of("nome")),
                tool("contar_atendimentos",
                        "Conta quantos atendimentos aconteceram no mes atual, opcionalmente filtrando por especie.",
                        Map.of("especie", Map.of(
                                "type", List.of("string", "null"),
                                "description", "'dog' para caes, 'cat' para gatos, ou null para todas as especies")),
                        List.of("especie")),
                tool("contar_retornos_pendentes",
                        "Conta quantos atendimentos tem retorno marcado que ainda nao foi feito.",
                        Map.of(),
                        List.of())
        );
    }

    private static Map<String, Object> tool(String name, String description, Map<String, Object> properties, List<String> required) {
        return Map.of(
                "type", "function",
                "function", Map.of(
                        "name", name,
                        "description", description,
                        "parameters", Map.of(
                                "type", "object",
                                "properties", properties,
                                "required", required
                        )
                )
        );
    }
}