package com.example.service_management.features.productapplication.controller;

import com.example.service_management.features.productapplication.dto.ProductApplicationRequestDTO;
import com.example.service_management.features.productapplication.dto.ProductApplicationResponseDTO;
import com.example.service_management.features.productapplication.service.ProductApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-applications")
public class ProductApplicationController {

    private final ProductApplicationService productApplicationService;

    public ProductApplicationController(ProductApplicationService productApplicationService) {
        this.productApplicationService = productApplicationService;
    }

    @GetMapping
    public List<ProductApplicationResponseDTO> getAll() {
        return productApplicationService.findAll();
    }

    @GetMapping("/patient/{patientId}")
    public List<ProductApplicationResponseDTO> getByPatientId(@PathVariable Long patientId) {
        return productApplicationService.findByPatientId(patientId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductApplicationResponseDTO create(@Valid @RequestBody ProductApplicationRequestDTO dto) {
        return productApplicationService.create(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productApplicationService.delete(id);
    }
}
