package service;

import model.Account;
import model.Customer;

import java.util.List;

public interface CustomerService {
    boolean registerCustomer(Customer customer);
    Customer getCustomer(String customerId);
    boolean customerExists(String customerId);
    boolean addAccountToCustomer(String customerId, Account account);
    List<Account> getCustomerAccounts(String customerId);
    List<Customer> getAllCustomers();
}