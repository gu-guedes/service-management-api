package com.example.service_management.features.medicalrecord.repository;

import com.example.service_management.features.medicalrecord.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPatientIdOrderByRecordDateDesc(Long patientId);

    // esconde atendimentos de pacientes ja excluidos (soft delete) — mesmo padrao do
    // PatientRepository.findAllByDeletedFalse()
    List<MedicalRecord> findAllByPatient_DeletedFalse();
}
