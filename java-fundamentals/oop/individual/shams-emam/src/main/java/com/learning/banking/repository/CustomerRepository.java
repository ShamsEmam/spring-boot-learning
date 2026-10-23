package com.learning.banking.repository;

import com.learning.banking.model.Customer;

import java.util.List;

/**
 * Storage for customers.
 *
 * save() inserts OR updates. Rules such as "no duplicate IDs" belong to the
 * service layer so that every implementation behaves identically.
 */
public interface CustomerRepository {

    void save(Customer customer);

    Customer findById(String customerId);

    boolean existsById(String customerId);

    List<Customer> findAll();
}