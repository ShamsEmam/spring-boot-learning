package com.learning.banking.repository.inmemory;

import com.learning.banking.model.Customer;
import com.learning.banking.repository.CustomerRepository;
import com.learning.banking.util.Validator;

import java.util.ArrayList;
import java.util.List;

public class InMemoryCustomerRepository implements CustomerRepository {

    private final List<Customer> customers = new ArrayList<>();

    @Override
    public void save(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getCustomerId().equals(customer.getCustomerId())) {
                customers.set(i, customer);
                return;
            }
        }

        customers.add(customer);
    }

    @Override
    public Customer findById(String customerId) {
        Validator.requireNotBlank(customerId, "Customer ID");

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
}
