package com.example.service_management.service;

import com.example.service_management.model.Customer;
import com.example.service_management.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository repo;
    public CustomerService(CustomerRepository repo) {
        this.repo = repo;
    }

    public List<Customer> findAll() {
        return repo.findAll();
    }

    public Customer create(Customer customer) {
        return repo.save(customer);
    }
}
