package com.example.service_management.controller;

import com.example.service_management.dto.CustomerRequestDTO;
import com.example.service_management.dto.CustomerResponseDTO;
import com.example.service_management.exception.ResourceNotFoundException;
import com.example.service_management.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;
    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    public List<CustomerResponseDTO> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO>  getById(@PathVariable Long id) {
CustomerResponseDTO dto = service.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        return ResponseEntity.ok(dto);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponseDTO create(@Valid @RequestBody CustomerRequestDTO customer) {
        return service.create(customer);
    }
}
