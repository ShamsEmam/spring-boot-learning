package com.learning.banking.service.impl;

import com.learning.banking.model.Customer;
import com.learning.banking.repository.CustomerRepository;
import com.learning.banking.service.CustomerService;

public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(
            CustomerRepository customerRepository) {

        if (customerRepository == null) {
            throw new IllegalArgumentException(
                    "Customer repository cannot be null"
            );
        }

        this.customerRepository = customerRepository;
    }

    @Override
    public void registerCustomer(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer cannot be null"
            );
        }

        customerRepository.save(customer);
    }

    @Override
    public Customer findCustomerById(String customerId) {
        return customerRepository.findById(customerId);
    }
}