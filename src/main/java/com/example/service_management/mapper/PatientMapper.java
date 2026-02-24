package com.example.service_management.mapper;

import com.example.service_management.dto.PatientRequestDTO;
import com.example.service_management.dto.PatientResponseDTO;
import com.example.service_management.model.Customer;
import com.example.service_management.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public PatientResponseDTO toResponse(Patient p) {
        if (p == null) return null;
        return PatientResponseDTO.builder()
                .id(p.getId())
                .customerId(p.getCustomer() != null ? p.getCustomer().getId() : null)
                .name(p.getName())
                .species(p.getSpecies())
                .breed(p.getBreed())
                .sex(p.getSex())
                .birthDate(p.getBirthDate())
                .weightKg(p.getWeightKg())
                .neutered(p.isNeutered())
                .notes(p.getNotes())
                .active(p.isActive())
                .build();
    }

    public Patient toEntity(PatientRequestDTO dto, Customer customer) {
        Patient p = new Patient();
        p.setCustomer(customer);
        p.setName(dto.getName());
        p.setSpecies(dto.getSpecies());
        p.setBreed(dto.getBreed());
        p.setSex(dto.getSex());
        p.setBirthDate(dto.getBirthDate());
        p.setWeightKg(dto.getWeightKg());
        p.setNeutered(dto.isNeutered());
        p.setNotes(dto.getNotes());
        p.setActive(dto.isActive());
        return p;
    }

    public void updateEntity(Patient existing, PatientRequestDTO dto, Customer customer) {
        existing.setCustomer(customer);
        existing.setName(dto.getName());
        existing.setSpecies(dto.getSpecies());
        existing.setBreed(dto.getBreed());
        existing.setSex(dto.getSex());
        existing.setBirthDate(dto.getBirthDate());
        existing.setWeightKg(dto.getWeightKg());
        existing.setNeutered(dto.isNeutered());
        existing.setNotes(dto.getNotes());
        existing.setActive(dto.isActive());
    }
}
