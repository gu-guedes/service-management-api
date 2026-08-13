package com.example.service_management.features.whatsappbot.service;

import com.example.service_management.features.medicalrecord.model.MedicalRecord;
import com.example.service_management.features.medicalrecord.repository.MedicalRecordRepository;
import com.example.service_management.features.patient.model.Patient;
import com.example.service_management.features.patient.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ChatQueryService {

    private static final int HISTORICO_MAX_ITEMS = 5;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final ZoneId ZONE = ZoneId.of("America/Sao_Paulo");

    private static final String HELP_MESSAGE = "Comando nao reconhecido. Comandos disponiveis:\n"
            + "/historico <nome do pet>\n"
            + "/quantidade cachorros mes\n"
            + "/quantidade gatos mes\n"
            + "/quantidade atendimentos mes\n"
            + "/quantidade retornos pendentes";

    private final PatientRepository patientRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public ChatQueryService(PatientRepository patientRepository, MedicalRecordRepository medicalRecordRepository) {
        this.patientRepository = patientRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public String execute(ChatCommand command) {
        return switch (command.intent()) {
            case HISTORICO -> handleHistorico(command.petName());
            case QUANTIDADE_CACHORROS_MES -> handleQuantidadePorEspecie("dog", "cachorros");
            case QUANTIDADE_GATOS_MES -> handleQuantidadePorEspecie("cat", "gatos");
            case QUANTIDADE_ATENDIMENTOS_MES -> handleQuantidadeAtendimentosMes();
            case QUANTIDADE_RETORNOS_PENDENTES -> handleQuantidadeRetornosPendentes();
            case UNKNOWN -> HELP_MESSAGE;
        };
    }

    private String handleHistorico(String petName) {
        if (petName == null || petName.isBlank()) {
            return "Uso: /historico <nome do pet>";
        }

        Optional<Patient> patientOpt = patientRepository.findFirstByNameIgnoreCaseAndDeletedFalse(petName);
        if (patientOpt.isEmpty()) {
            return "Nenhum pet encontrado com o nome \"" + petName + "\".";
        }

        Patient patient = patientOpt.get();
        List<MedicalRecord> records = medicalRecordRepository.findByPatientIdOrderByRecordDateDesc(patient.getId());

        if (records.isEmpty()) {
            return "Nenhum atendimento registrado pra " + patient.getName() + ".";
        }

        StringBuilder sb = new StringBuilder("Historico de " + patient.getName() + ":\n");
        records.stream()
                .limit(HISTORICO_MAX_ITEMS)
                .forEach(record -> sb.append("\n")
                        .append(DATE_FORMAT.format(record.getRecordDate()))
                        .append(" - Queixa: ").append(record.getComplaint())
                        .append(" | Tratamento: ").append(record.getTreatment()));

        return sb.toString();
    }

    private String handleQuantidadePorEspecie(String species, String label) {
        OffsetDateTime[] range = currentMonthRange();
        long count = medicalRecordRepository.countByPatientSpeciesIgnoreCaseAndRecordDateBetween(species, range[0], range[1]);
        return "Atendimentos de " + label + " nesse mes: " + count;
    }

    private String handleQuantidadeAtendimentosMes() {
        OffsetDateTime[] range = currentMonthRange();
        long count = medicalRecordRepository.countByRecordDateBetween(range[0], range[1]);
        return "Total de atendimentos nesse mes: " + count;
    }

    private String handleQuantidadeRetornosPendentes() {
        long count = medicalRecordRepository.countByFollowUpDoneFalseAndFollowUpDateIsNotNull();
        return "Retornos pendentes: " + count;
    }

    private OffsetDateTime[] currentMonthRange() {
        OffsetDateTime now = OffsetDateTime.now(ZONE);
        OffsetDateTime start = now.withDayOfMonth(1).toLocalDate().atStartOfDay(ZONE).toOffsetDateTime();
        return new OffsetDateTime[]{start, now};
    }
}