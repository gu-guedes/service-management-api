package com.example.service_management.service;

import com.example.service_management.exception.ResourceNotFoundException;
import com.example.service_management.mapper.CustomerMapper;
import com.example.service_management.model.Customer;
import com.example.service_management.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import com.example.service_management.dto.CustomerResponseDTO;
import com.example.service_management.dto.CustomerRequestDTO;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository repo;
    private final CustomerMapper mapper;

    public CustomerService(CustomerRepository repo, CustomerMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<CustomerResponseDTO> findAll() {
        return repo.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public CustomerResponseDTO findById(Long id) {
        Customer customer = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        return mapper.toResponseDTO(customer);

    }

    public CustomerResponseDTO create(CustomerRequestDTO dto) {
        Customer customer = mapper.toEntity(dto);
        Customer savedCustomer = repo.save(customer);
        return mapper.toResponseDTO(savedCustomer);

    }

    public CustomerResponseDTO update(Long id, CustomerRequestDTO dto) {
       Customer customer = repo.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));

       mapper.updateEntity(customer, dto);

       Customer updatedCustomer = repo.save(customer);
       return mapper.toResponseDTO(updatedCustomer);
    }

    public void delete(Long id) {
        Customer existingCustomer = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        repo.delete(existingCustomer);
    }

}
