package com.learning.banking.service;

import com.learning.banking.model.Customer;

public interface CustomerService {

    void registerCustomer(Customer customer);

    Customer findCustomerById(String customerId);
}