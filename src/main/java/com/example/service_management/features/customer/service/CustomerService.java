package com.example.service_management.features.customer.service;

import com.example.service_management.exception.ResourceNotFoundException;
import com.example.service_management.features.customer.mapper.CustomerMapper;
import com.example.service_management.features.customer.model.Customer;
import com.example.service_management.features.customer.repository.CustomerRepository;
import com.example.service_management.features.patient.model.Patient;
import com.example.service_management.features.patient.repository.PatientRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.example.service_management.features.customer.dto.CustomerResponseDTO;
import com.example.service_management.features.customer.dto.CustomerRequestDTO;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository repo;
    private final CustomerMapper mapper;
    private final PatientRepository patientRepository;

    public CustomerService(CustomerRepository repo, CustomerMapper mapper, PatientRepository patientRepository) {
        this.repo = repo;
        this.mapper = mapper;
        this.patientRepository = patientRepository;
    }

    public List<CustomerResponseDTO> findAll() {
        return repo.findAllByDeletedFalse()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public CustomerResponseDTO findById(Long id) {
        Customer customer = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        return mapper.toResponseDTO(customer);

    }
    @Transactional
    public CustomerResponseDTO create(CustomerRequestDTO dto) {
        Customer customer = mapper.toEntity(dto);
        Customer savedCustomer = repo.save(customer);
        return mapper.toResponseDTO(savedCustomer);

    }
    @Transactional
    public CustomerResponseDTO update(Long id, CustomerRequestDTO dto) {
       Customer customer = repo.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));

       mapper.updateEntity(customer, dto);

       Customer updatedCustomer = repo.save(customer);
       return mapper.toResponseDTO(updatedCustomer);
    }
    // "Excluir" — some da lista de vez (tutor e todos os pets dele), mas nao apaga a linha
    // (preserva historico); nao mexe nos dados, so a flag "deleted" em cascata
    @Transactional
    public void delete(Long id) {
        Customer existingCustomer = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        existingCustomer.setDeleted(true);

        List<Patient> pets = patientRepository.findByCustomerIdAndDeletedFalse(id);
        pets.forEach(pet -> pet.setDeleted(true));
    }

}
