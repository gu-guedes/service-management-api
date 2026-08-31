package com.example.service_management.features.examrequest.mapper;

import com.example.service_management.features.examrequest.dto.ExamRequestRequestDTO;
import com.example.service_management.features.examrequest.dto.ExamRequestResponseDTO;
import com.example.service_management.features.examrequest.model.ExamRequest;
import com.example.service_management.features.medicalrecord.model.MedicalRecord;
import org.springframework.stereotype.Component;

@Component
public class ExamRequestMapper {

    public ExamRequestResponseDTO toResponse(ExamRequest e) {
        if (e == null) return null;
        MedicalRecord record = e.getMedicalRecord();
        return ExamRequestResponseDTO.builder()
                .id(e.getId())
                .medicalRecordId(record != null ? record.getId() : null)
                .patientId(record != null && record.getPatient() != null ? record.getPatient().getId() : null)
                .patientName(record != null && record.getPatient() != null ? record.getPatient().getName() : null)
                .examName(e.getExamName())
                .requestedDate(e.getRequestedDate())
                .resultFileName(e.getResultFileName())
                .resultUploadedAt(e.getResultUploadedAt())
                .createdAt(e.getCreatedAt())
                .build();
    }

    public ExamRequest toEntity(ExamRequestRequestDTO dto, MedicalRecord medicalRecord) {
        return new ExamRequest(medicalRecord, dto.getExamName(), dto.getRequestedDate());
    }
}
