package com.learning.banking.repository.inmemory;

import com.learning.banking.model.Customer;
import com.learning.banking.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryCustomerRepository
        implements CustomerRepository {

    private final List<Customer> customers =
            new ArrayList<>();

    @Override
    public void save(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer cannot be null"
            );
        }

        if (existsById(customer.getCustomerId())) {
            throw new IllegalArgumentException(
                    "Customer ID already exists"
            );
        }

        customers.add(customer);
    }

    @Override
    public Customer findById(String customerId) {

        validateId(customerId);

        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }

        return null;
    }

    @Override
    public boolean existsById(String customerId) {
        return findById(customerId) != null;
    }

    @Override
    public List<Customer> findAll() {
        return List.copyOf(customers);
    }

    private void validateId(String customerId) {

        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException(
                    "Customer ID cannot be null or blank"
            );
        }
    }
}