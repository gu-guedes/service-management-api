package com.example.service_management.features.examrequest.repository;

import com.example.service_management.features.examrequest.model.ExamRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRequestRepository extends JpaRepository<ExamRequest, Long> {
    List<ExamRequest> findByMedicalRecordIdOrderByCreatedAtDesc(Long medicalRecordId);
}
