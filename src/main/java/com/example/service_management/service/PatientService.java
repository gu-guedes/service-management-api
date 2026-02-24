package com.example.service_management.service;

import com.example.service_management.dto.PatientRequestDTO;
import com.example.service_management.dto.PatientResponseDTO;
import com.example.service_management.exception.ResourceNotFoundException;
import com.example.service_management.mapper.PatientMapper;
import com.example.service_management.model.Customer;
import com.example.service_management.model.Patient;
import com.example.service_management.repository.CustomerRepository;
import com.example.service_management.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final CustomerRepository customerRepository;

    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper, CustomerRepository customerRepository) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.customerRepository = customerRepository;
    }


    public List<PatientResponseDTO> findAll() {
        return patientRepository.findAll().stream()
                .map(patientMapper::toResponse)
                .toList();
    }

    public PatientResponseDTO findById(Long id) {
        Patient p = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + id));
        return patientMapper.toResponse(p);
    }

    @Transactional
    public PatientResponseDTO create(PatientRequestDTO dto) {
        Customer customer = customerOrFail(dto.getCustomerId());
        Patient p = patientMapper.toEntity(dto, customer);
        Patient saved = patientRepository.save(p);
        return patientMapper.toResponse(saved);
    }

    @Transactional
    public PatientResponseDTO update(Long id, PatientRequestDTO dto) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + id));

        Customer customer = customerOrFail(dto.getCustomerId());
        patientMapper.updateEntity(existing, dto, customer);

        return patientMapper.toResponse(existing);
    }

    @Transactional
    public void delete(Long id) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + id));

        existing.setActive(false);

    }

    private Customer customerOrFail(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerId));
    }
}
