package com.example.service_management.controller;

import com.example.service_management.dto.CustomerRequestDTO;
import com.example.service_management.dto.CustomerResponseDTO;
import com.example.service_management.service.CustomerService;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponseDTO create(@RequestBody CustomerRequestDTO customer) {
        return service.create(customer);
    }
}
