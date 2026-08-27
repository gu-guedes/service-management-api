package com.example.service_management.features.customer.dto;

// grupo de validacao usado so na criacao do cliente/tutor — permite exigir o
// CPF apenas em cadastros novos, sem quebrar a edicao de clientes ja existentes
// que ainda nao tem CPF preenchido (ver CustomerController#create)
public interface OnCreate {
}
