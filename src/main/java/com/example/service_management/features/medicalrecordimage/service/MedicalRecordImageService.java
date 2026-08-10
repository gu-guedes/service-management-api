package com.example.service_management.features.medicalrecordimage.service;

import com.example.service_management.exception.ResourceNotFoundException;
import com.example.service_management.features.medicalrecord.model.MedicalRecord;
import com.example.service_management.features.medicalrecord.repository.MedicalRecordRepository;
import com.example.service_management.features.medicalrecordimage.dto.MedicalRecordImageResponseDTO;
import com.example.service_management.features.medicalrecordimage.mapper.MedicalRecordImageMapper;
import com.example.service_management.features.medicalrecordimage.model.MedicalRecordImage;
import com.example.service_management.features.medicalrecordimage.repository.MedicalRecordImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MedicalRecordImageService {

    private final MedicalRecordImageRepository imageRepository;
    private final MedicalRecordImageMapper imageMapper;
    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordImageService(MedicalRecordImageRepository imageRepository,
                                      MedicalRecordImageMapper imageMapper,
                                      MedicalRecordRepository medicalRecordRepository) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public List<MedicalRecordImageResponseDTO> findAll() {
        return imageRepository.findAll().stream()
                .map(imageMapper::toResponse)
                .toList();
    }

    public List<MedicalRecordImageResponseDTO> findByMedicalRecordId(Long medicalRecordId) {
        medicalRecordOrFail(medicalRecordId);
        return imageRepository.findByMedicalRecordIdOrderByCreatedAtDesc(medicalRecordId).stream()
                .map(imageMapper::toResponse)
                .toList();
    }

    @Transactional
    public MedicalRecordImageResponseDTO upload(Long medicalRecordId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }
        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are accepted");
        }

        MedicalRecord medicalRecord = medicalRecordOrFail(medicalRecordId);

        byte[] data;
        try {
            data = file.getBytes();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read uploaded file", e);
        }

        MedicalRecordImage image = new MedicalRecordImage(
                medicalRecord,
                file.getOriginalFilename(),
                file.getContentType(),
                data
        );

        MedicalRecordImage saved = imageRepository.save(image);
        return imageMapper.toResponse(saved);
    }

    public MedicalRecordImage getEntityWithImage(Long id) {
        return imageOrFail(id);
    }

    @Transactional
    public void delete(Long id) {
        MedicalRecordImage image = imageOrFail(id);
        imageRepository.delete(image);
    }

    private MedicalRecordImage imageOrFail(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record image not found: " + id));
    }

    private MedicalRecord medicalRecordOrFail(Long medicalRecordId) {
        return medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found: " + medicalRecordId));
    }
}
