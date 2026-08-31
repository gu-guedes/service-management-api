package com.example.service_management.features.medicalrecordimage.mapper;

import com.example.service_management.features.medicalrecordimage.dto.MedicalRecordImageResponseDTO;
import com.example.service_management.features.medicalrecordimage.model.MedicalRecordImage;
import com.example.service_management.features.medicalrecord.model.MedicalRecord;
import org.springframework.stereotype.Component;

@Component
public class MedicalRecordImageMapper {

    public MedicalRecordImageResponseDTO toResponse(MedicalRecordImage img) {
        if (img == null) return null;
        MedicalRecord record = img.getMedicalRecord();
        return MedicalRecordImageResponseDTO.builder()
                .id(img.getId())
                .medicalRecordId(record != null ? record.getId() : null)
                .patientId(record != null && record.getPatient() != null ? record.getPatient().getId() : null)
                .patientName(record != null && record.getPatient() != null ? record.getPatient().getName() : null)
                .fileName(img.getFileName())
                .contentType(img.getContentType())
                .createdAt(img.getCreatedAt())
                .build();
    }
}