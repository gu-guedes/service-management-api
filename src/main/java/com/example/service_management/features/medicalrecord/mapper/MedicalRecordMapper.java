package com.example.service_management.features.medicalrecord.mapper;

import com.example.service_management.features.medicalrecord.dto.MedicalRecordRequestDTO;
import com.example.service_management.features.medicalrecord.dto.MedicalRecordResponseDTO;
import com.example.service_management.features.medicalrecord.model.MedicalRecord;
import com.example.service_management.features.patient.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class MedicalRecordMapper {

    public MedicalRecordResponseDTO toResponse(MedicalRecord r) {
        if (r == null) return null;
        return MedicalRecordResponseDTO.builder()
                .id(r.getId())
                .patientId(r.getPatient() != null ? r.getPatient().getId() : null)
                .patientName(r.getPatient() != null ? r.getPatient().getName() : null)
                .recordDate(r.getRecordDate())
                .complaint(r.getComplaint())
                .anamnesis(r.getAnamnesis())
                .treatment(r.getTreatment())
                .weightKg(r.getWeightKg())
                .createdAt(r.getCreatedAt())
                .build();
    }

    public MedicalRecord toEntity(MedicalRecordRequestDTO dto, Patient patient) {
        MedicalRecord r = new MedicalRecord();
        r.setPatient(patient);
        r.setRecordDate(dto.getRecordDate());
        r.setComplaint(dto.getComplaint());
        r.setAnamnesis(dto.getAnamnesis());
        r.setTreatment(dto.getTreatment());
        r.setWeightKg(dto.getWeightKg());
        return r;
    }

    public void updateEntity(MedicalRecord existing, MedicalRecordRequestDTO dto, Patient patient) {
        existing.setPatient(patient);
        if (dto.getRecordDate() != null) {
            existing.setRecordDate(dto.getRecordDate());
        }
        existing.setComplaint(dto.getComplaint());
        existing.setAnamnesis(dto.getAnamnesis());
        existing.setTreatment(dto.getTreatment());
        existing.setWeightKg(dto.getWeightKg());
    }
}
