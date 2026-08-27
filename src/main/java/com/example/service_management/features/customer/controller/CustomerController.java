package com.example.service_management.features.customer.controller;

import com.example.service_management.features.customer.dto.CustomerRequestDTO;
import com.example.service_management.features.customer.dto.CustomerResponseDTO;
import com.example.service_management.features.customer.dto.OnCreate;
import com.example.service_management.features.customer.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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
    public List<CustomerResponseDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public CustomerResponseDTO getById(@PathVariable Long id) {

        return service.findById(id);
    }

    // CPF e obrigatorio so na criacao (grupo OnCreate) — clientes ja cadastrados
    // sem CPF continuam podendo ser atualizados normalmente (ver update abaixo,
    // que usa @Valid puro e nao aplica esse grupo extra)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponseDTO create(@Validated({Default.class, OnCreate.class}) @RequestBody CustomerRequestDTO customer) {
        return service.create(customer);
    }

    @PutMapping("/{id}")
    public CustomerResponseDTO update(@PathVariable Long id, @Valid @RequestBody CustomerRequestDTO customer) {
        return service.update(id, customer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
