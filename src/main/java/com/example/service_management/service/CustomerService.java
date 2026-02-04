package com.example.service_management.service;

import com.example.service_management.model.Customer;
import com.example.service_management.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import com.example.service_management.dto.CustomerResponseDTO;
import com.example.service_management.dto.CustomerRequestDTO;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository repo;
    public CustomerService(CustomerRepository repo) {
        this.repo = repo;
    }

    public List<CustomerResponseDTO> findAll() {
        return repo.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public Optional<CustomerResponseDTO> findById(Long id) {
        return repo.findById(id)
                .map(this::toResponseDTO);
    }

    public CustomerResponseDTO create(CustomerRequestDTO dto) {
        Customer customer = new Customer(
                dto.getName(),
                dto.getEmail(),
                dto.getPhone()
        );
        Customer savedCustomer = repo.save(customer);
        return toResponseDTO(savedCustomer);

    }
    private CustomerResponseDTO toResponseDTO(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getCreatedAt()
        );
    }
}
