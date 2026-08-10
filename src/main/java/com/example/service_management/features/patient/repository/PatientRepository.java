package com.example.service_management.features.patient.repository;

import com.example.service_management.features.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findAllByDeletedFalse();

    List<Patient> findByCustomerIdAndDeletedFalse(Long customerId);
}
