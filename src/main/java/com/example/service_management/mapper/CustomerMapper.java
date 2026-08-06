package com.example.service_management.mapper;

import com.example.service_management.dto.CustomerRequestDTO;
import com.example.service_management.dto.CustomerResponseDTO;
import com.example.service_management.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerResponseDTO toResponseDTO(Customer customer) {
        return new CustomerResponseDTO(
            customer.getId(),
            customer.getName(),
            customer.getEmail(),
            customer.getPhone(),
            customer.getStreet(),
            customer.getStreetNumber(),
            customer.getNeighborhood(),
            customer.getCity(),
            customer.getReferencePoint(),
            customer.getCreatedAt(),
            customer.getUpdatedAt()
        );
    }

    public Customer toEntity(CustomerRequestDTO dto) {
        return new Customer(dto.getName(), dto.getEmail(), dto.getPhone(), dto.getStreet(), dto.getStreetNumber(),
                dto.getNeighborhood(), dto.getCity(), dto.getReferencePoint());
    }

    public void updateEntity(Customer customer, CustomerRequestDTO dto) {
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setStreet(dto.getStreet());
        customer.setStreetNumber(dto.getStreetNumber());
        customer.setNeighborhood(dto.getNeighborhood());
        customer.setCity(dto.getCity());
        customer.setReferencePoint(dto.getReferencePoint());
    }

}
