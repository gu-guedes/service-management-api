package com.example.service_management.features.examrequest.service;

import com.example.service_management.exception.ResourceNotFoundException;
import com.example.service_management.features.examrequest.dto.ExamRequestRequestDTO;
import com.example.service_management.features.examrequest.dto.ExamRequestResponseDTO;
import com.example.service_management.features.examrequest.mapper.ExamRequestMapper;
import com.example.service_management.features.examrequest.model.ExamRequest;
import com.example.service_management.features.examrequest.repository.ExamRequestRepository;
import com.example.service_management.features.medicalrecord.model.MedicalRecord;
import com.example.service_management.features.medicalrecord.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ExamRequestService {

    private final ExamRequestRepository examRequestRepository;
    private final ExamRequestMapper examRequestMapper;
    private final MedicalRecordRepository medicalRecordRepository;

    public ExamRequestService(ExamRequestRepository examRequestRepository,
                               ExamRequestMapper examRequestMapper,
                               MedicalRecordRepository medicalRecordRepository) {
        this.examRequestRepository = examRequestRepository;
        this.examRequestMapper = examRequestMapper;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public List<ExamRequestResponseDTO> findAll() {
        return examRequestRepository.findAll().stream()
                .map(examRequestMapper::toResponse)
                .toList();
    }

    public List<ExamRequestResponseDTO> findByMedicalRecordId(Long medicalRecordId) {
        medicalRecordOrFail(medicalRecordId);
        return examRequestRepository.findByMedicalRecordIdOrderByCreatedAtDesc(medicalRecordId).stream()
                .map(examRequestMapper::toResponse)
                .toList();
    }

    @Transactional
    public ExamRequestResponseDTO create(ExamRequestRequestDTO dto) {
        MedicalRecord medicalRecord = medicalRecordOrFail(dto.getMedicalRecordId());
        ExamRequest exam = examRequestMapper.toEntity(dto, medicalRecord);
        ExamRequest saved = examRequestRepository.save(exam);
        return examRequestMapper.toResponse(saved);
    }

    @Transactional
    public ExamRequestResponseDTO uploadResult(Long id, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }
        if (!"application/pdf".equals(file.getContentType())) {
            throw new IllegalArgumentException("Only PDF files are accepted");
        }

        ExamRequest exam = examRequestOrFail(id);

        try {
            exam.setResultFile(file.getBytes());
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read uploaded file", e);
        }
        exam.setResultFileName(file.getOriginalFilename());
        exam.setResultUploadedAt(OffsetDateTime.now());

        ExamRequest saved = examRequestRepository.save(exam);
        return examRequestMapper.toResponse(saved);
    }

    public ExamRequest getEntityWithResult(Long id) {
        ExamRequest exam = examRequestOrFail(id);
        if (exam.getResultFile() == null) {
            throw new ResourceNotFoundException("Exam result not uploaded yet: " + id);
        }
        return exam;
    }

    @Transactional
    public void delete(Long id) {
        ExamRequest exam = examRequestOrFail(id);
        examRequestRepository.delete(exam);
    }

    private ExamRequest examRequestOrFail(Long id) {
        return examRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam request not found: " + id));
    }

    private MedicalRecord medicalRecordOrFail(Long medicalRecordId) {
        return medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found: " + medicalRecordId));
    }
}
