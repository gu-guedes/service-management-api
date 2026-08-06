package com.example.service_management.features.customer.repository;

import com.example.service_management.features.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
