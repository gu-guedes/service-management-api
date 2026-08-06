package com.example.service_management.features.medicalrecord.service;

import com.example.service_management.features.medicalrecord.dto.MedicalRecordRequestDTO;
import com.example.service_management.features.medicalrecord.dto.MedicalRecordResponseDTO;
import com.example.service_management.exception.ResourceNotFoundException;
import com.example.service_management.features.medicalrecord.mapper.MedicalRecordMapper;
import com.example.service_management.features.medicalrecord.model.MedicalRecord;
import com.example.service_management.features.patient.model.Patient;
import com.example.service_management.features.medicalrecord.repository.MedicalRecordRepository;
import com.example.service_management.features.patient.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordMapper medicalRecordMapper;
    private final PatientRepository patientRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository,
                                 MedicalRecordMapper medicalRecordMapper,
                                 PatientRepository patientRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.medicalRecordMapper = medicalRecordMapper;
        this.patientRepository = patientRepository;
    }

    public List<MedicalRecordResponseDTO> findAll() {
        return medicalRecordRepository.findAll().stream()
                .map(medicalRecordMapper::toResponse)
                .toList();
    }

    public List<MedicalRecordResponseDTO> findByPatientId(Long patientId) {
        patientOrFail(patientId);
        return medicalRecordRepository.findByPatientIdOrderByRecordDateDesc(patientId).stream()
                .map(medicalRecordMapper::toResponse)
                .toList();
    }

    public MedicalRecordResponseDTO findById(Long id) {
        MedicalRecord r = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found: " + id));
        return medicalRecordMapper.toResponse(r);
    }

    @Transactional
    public MedicalRecordResponseDTO create(MedicalRecordRequestDTO dto) {
        Patient patient = patientOrFail(dto.getPatientId());
        MedicalRecord r = medicalRecordMapper.toEntity(dto, patient);
        MedicalRecord saved = medicalRecordRepository.save(r);
        return medicalRecordMapper.toResponse(saved);
    }

    @Transactional
    public MedicalRecordResponseDTO update(Long id, MedicalRecordRequestDTO dto) {
        MedicalRecord existing = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found: " + id));

        Patient patient = patientOrFail(dto.getPatientId());
        medicalRecordMapper.updateEntity(existing, dto, patient);
        MedicalRecord saved = medicalRecordRepository.save(existing);

        return medicalRecordMapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        MedicalRecord existing = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found: " + id));
        medicalRecordRepository.delete(existing);
    }

    private Patient patientOrFail(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + patientId));
    }
}
