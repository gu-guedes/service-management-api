package com.example.service_management.features.medicalrecord.repository;

import com.example.service_management.features.medicalrecord.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPatientIdOrderByRecordDateDesc(Long patientId);

    long countByRecordDateBetween(OffsetDateTime start, OffsetDateTime end);

    long countByPatientSpeciesIgnoreCaseAndRecordDateBetween(String species, OffsetDateTime start, OffsetDateTime end);

    long countByFollowUpDoneFalseAndFollowUpDateIsNotNull();
}
