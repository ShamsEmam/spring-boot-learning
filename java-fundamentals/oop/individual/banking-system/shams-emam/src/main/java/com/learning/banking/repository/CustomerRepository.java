package com.learning.banking.repository;

import com.learning.banking.model.Customer;

import java.util.List;

public interface CustomerRepository {

    void save(Customer customer);

    Customer findById(String customerId);

    boolean existsById(String customerId);

    List<Customer> findAll();
}