package com.example.service_management.features.medicalrecordimage.repository;

import com.example.service_management.features.medicalrecordimage.model.MedicalRecordImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalRecordImageRepository extends JpaRepository<MedicalRecordImage, Long> {
    List<MedicalRecordImage> findByMedicalRecordIdOrderByCreatedAtDesc(Long medicalRecordId);
}