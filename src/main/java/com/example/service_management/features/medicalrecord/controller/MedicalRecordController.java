package com.example.service_management.features.medicalrecord.controller;

import com.example.service_management.features.medicalrecord.dto.MedicalRecordRequestDTO;
import com.example.service_management.features.medicalrecord.dto.MedicalRecordResponseDTO;
import com.example.service_management.features.medicalrecord.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping
    public List<MedicalRecordResponseDTO> getAll() {
        return medicalRecordService.findAll();
    }

    @GetMapping("/{id}")
    public MedicalRecordResponseDTO getById(@PathVariable Long id) {
        return medicalRecordService.findById(id);
    }

    @GetMapping("/patient/{patientId}")
    public List<MedicalRecordResponseDTO> getByPatientId(@PathVariable Long patientId) {
        return medicalRecordService.findByPatientId(patientId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicalRecordResponseDTO create(@Valid @RequestBody MedicalRecordRequestDTO dto) {
        return medicalRecordService.create(dto);
    }

    @PutMapping("/{id}")
    public MedicalRecordResponseDTO update(@PathVariable Long id, @Valid @RequestBody MedicalRecordRequestDTO dto) {
        return medicalRecordService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        medicalRecordService.delete(id);
    }
}
