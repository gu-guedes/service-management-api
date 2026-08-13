package com.example.service_management.features.whatsappbot.service;

import com.example.service_management.features.medicalrecord.model.MedicalRecord;
import com.example.service_management.features.medicalrecord.repository.MedicalRecordRepository;
import com.example.service_management.features.patient.model.Patient;
import com.example.service_management.features.patient.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ChatToolExecutor {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final ZoneId ZONE = ZoneId.of("America/Sao_Paulo");
    private static final int HISTORICO_MAX_ITEMS = 5;

    private final PatientRepository patientRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public ChatToolExecutor(PatientRepository patientRepository, MedicalRecordRepository medicalRecordRepository) {
        this.patientRepository = patientRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public Map<String, Object> execute(String toolName, JsonNode arguments) {
        return switch (toolName) {
            case "buscar_dados_pet" -> buscarDadosPet(arguments.path("nome").asText(null));
            case "buscar_historico_atendimentos" -> buscarHistorico(arguments.path("nome").asText(null));
            case "contar_atendimentos" -> contarAtendimentos(arguments.path("especie").asText(null));
            case "contar_retornos_pendentes" ->
                    Map.of("quantidade", medicalRecordRepository.countByFollowUpDoneFalseAndFollowUpDateIsNotNull());
            default -> Map.of("erro", "Ferramenta desconhecida: " + toolName);
        };
    }

    private Map<String, Object> buscarDadosPet(String nome) {
        if (nome == null || nome.isBlank()) {
            return Map.of("erro", "Nome do pet nao informado.");
        }

        Optional<Patient> patientOpt = patientRepository.findFirstByNameIgnoreCaseAndDeletedFalse(nome);
        if (patientOpt.isEmpty()) {
            return Map.of("erro", "Nenhum pet encontrado com o nome \"" + nome + "\".");
        }

        Patient patient = patientOpt.get();
        Map<String, Object> data = new java.util.HashMap<>();
        data.put("nome", patient.getName());
        data.put("especie", patient.getSpecies());
        data.put("raca", patient.getBreed());
        data.put("sexo", patient.getSex());
        data.put("idadeAnos", patient.getAgeYears());
        data.put("pesoKg", patient.getWeightKg());
        data.put("observacoes", patient.getNotes());
        data.put("tutor", patient.getCustomer().getName());
        return data;
    }

    private Map<String, Object> buscarHistorico(String nome) {
        if (nome == null || nome.isBlank()) {
            return Map.of("erro", "Nome do pet nao informado.");
        }

        Optional<Patient> patientOpt = patientRepository.findFirstByNameIgnoreCaseAndDeletedFalse(nome);
        if (patientOpt.isEmpty()) {
            return Map.of("erro", "Nenhum pet encontrado com o nome \"" + nome + "\".");
        }

        Patient patient = patientOpt.get();
        List<MedicalRecord> records = medicalRecordRepository.findByPatientIdOrderByRecordDateDesc(patient.getId());

        List<Map<String, Object>> atendimentos = records.stream()
                .limit(HISTORICO_MAX_ITEMS)
                .map(record -> Map.<String, Object>of(
                        "data", DATE_FORMAT.format(record.getRecordDate()),
                        "queixa", record.getComplaint(),
                        "tratamento", record.getTreatment()))
                .toList();

        return Map.of("pet", patient.getName(), "atendimentos", atendimentos);
    }

    private Map<String, Object> contarAtendimentos(String especie) {
        OffsetDateTime now = OffsetDateTime.now(ZONE);
        OffsetDateTime start = now.withDayOfMonth(1).toLocalDate().atStartOfDay(ZONE).toOffsetDateTime();

        long count = (especie == null || especie.isBlank())
                ? medicalRecordRepository.countByRecordDateBetween(start, now)
                : medicalRecordRepository.countByPatientSpeciesIgnoreCaseAndRecordDateBetween(especie, start, now);

        return Map.of("quantidade", count, "periodo", "mes atual");
    }
}