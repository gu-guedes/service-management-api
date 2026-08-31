package com.example.service_management.features.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class CustomerRequestDTO {
    @NotBlank(message = "name is mandatory")
    @Size(max = 100, message = "name must be at most 100 characters")
    private String name;

    @Size(max = 150, message = "email must be at most 150 characters")
    @Email(message = "email must be a valid email address")
    private String email;

    @Pattern(regexp = "^\\+?\\d{8,15}$", message = "phone must be a valid phone number")
    private String phone;

    @NotBlank(message = "cpf is mandatory", groups = OnCreate.class)
    @Pattern(regexp = "^\\d{11}$", message = "cpf must contain exactly 11 digits")
    private String cpf;

    @NotBlank(message = "street is mandatory")
    @Size(max = 150, message = "street must be at most 150 characters")
    private String street;

    @NotBlank(message = "streetNumber is mandatory")
    @Size(max = 20, message = "streetNumber must be at most 20 characters")
    private String streetNumber;

    @NotBlank(message = "neighborhood is mandatory")
    @Size(max = 100, message = "neighborhood must be at most 100 characters")
    private String neighborhood;

    @NotBlank(message = "city is mandatory")
    @Size(max = 100, message = "city must be at most 100 characters")
    private String city;

    @Size(max = 150, message = "referencePoint must be at most 150 characters")
    private String referencePoint;

    // obrigatorio so na criacao (mesmo padrao do cpf) — tutores ja cadastrados sem data de
    // nascimento continuam podendo ser editados normalmente
    @NotNull(message = "birthDate is mandatory", groups = OnCreate.class)
    private LocalDate birthDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {this.name = name;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(String referencePoint) {
        this.referencePoint = referencePoint;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
