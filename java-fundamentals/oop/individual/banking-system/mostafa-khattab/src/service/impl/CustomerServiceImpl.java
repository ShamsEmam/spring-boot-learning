package service.impl;

import model.Account;
import model.Customer;
import repository.CustomerRepository;
import service.CustomerService;

import java.util.Collections;
import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepo;

    public CustomerServiceImpl(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public boolean registerCustomer(Customer customer) {
        if (customer == null) {
            return false;
        }
        if (customerRepo.exists(customer.getCustomerId())) {
            return false;
        }
        return customerRepo.save(customer);
    }

    @Override
    public Customer getCustomer(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            return null;
        }
        return customerRepo.findById(customerId);
    }

    @Override
    public boolean customerExists(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            return false;
        }
        return customerRepo.exists(customerId);
    }

    @Override
    public List<Account> getCustomerAccounts(String customerId) {
        Customer customer = getCustomer(customerId);
        if (customer == null) {
            return Collections.emptyList();
        }
        return customer.getAccounts();
    }

    @Override
    public boolean addAccountToCustomer(String customerId, Account account) {
        if (account == null) {
            return false;
        }

        Customer customer = getCustomer(customerId);
        if (customer == null) {
            return false;
        }

        customer.internalAddAccount(account);
        return true;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }
}