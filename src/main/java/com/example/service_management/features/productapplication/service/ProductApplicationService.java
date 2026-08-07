package com.example.service_management.features.productapplication.service;

import com.example.service_management.features.productapplication.dto.ProductApplicationRequestDTO;
import com.example.service_management.features.productapplication.dto.ProductApplicationResponseDTO;
import com.example.service_management.exception.ResourceNotFoundException;
import com.example.service_management.features.productapplication.mapper.ProductApplicationMapper;
import com.example.service_management.features.productapplication.model.ProductApplication;
import com.example.service_management.features.patient.model.Patient;
import com.example.service_management.features.productapplication.repository.ProductApplicationRepository;
import com.example.service_management.features.patient.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductApplicationService {

    private final ProductApplicationRepository productApplicationRepository;
    private final ProductApplicationMapper productApplicationMapper;
    private final PatientRepository patientRepository;

    public ProductApplicationService(ProductApplicationRepository productApplicationRepository,
                                      ProductApplicationMapper productApplicationMapper,
                                      PatientRepository patientRepository) {
        this.productApplicationRepository = productApplicationRepository;
        this.productApplicationMapper = productApplicationMapper;
        this.patientRepository = patientRepository;
    }

    public List<ProductApplicationResponseDTO> findAll() {
        return productApplicationRepository.findAll().stream()
                .map(productApplicationMapper::toResponse)
                .toList();
    }

    public List<ProductApplicationResponseDTO> findByPatientId(Long patientId) {
        patientOrFail(patientId);
        return productApplicationRepository.findByPatientIdOrderByExpiresAtDesc(patientId).stream()
                .map(productApplicationMapper::toResponse)
                .toList();
    }

    @Transactional
    public ProductApplicationResponseDTO create(ProductApplicationRequestDTO dto) {
        Patient patient = patientOrFail(dto.getPatientId());
        ProductApplication p = productApplicationMapper.toEntity(dto, patient);
        ProductApplication saved = productApplicationRepository.save(p);
        return productApplicationMapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        ProductApplication existing = productApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product application not found: " + id));
        productApplicationRepository.delete(existing);
    }

    private Patient patientOrFail(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + patientId));
    }
}
