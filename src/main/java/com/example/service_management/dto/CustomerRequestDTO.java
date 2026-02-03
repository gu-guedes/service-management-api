package com.example.service_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CustomerRequestDTO {
    @NotBlank(message = "name is mandatory")
    @Size(max = 100, message = "name must be at most 100 characters")
    private String name;

    @NotBlank(message = "email is mandatory")
    @Size(max = 150, message = "email must be at most 150 characters")
    @Email(message = "email must be a valid email address")
    private String email;

    @Pattern(regexp = "^\\+?\\d{8,15}$", message = "phone must be a valid phone number")
    private String phone;

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
}
