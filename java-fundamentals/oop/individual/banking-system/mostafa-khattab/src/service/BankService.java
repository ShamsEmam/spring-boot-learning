package service;

import model.Account;
import model.Customer;

public interface BankService {
    boolean registerCustomer(Customer customer);
    boolean openAccount(Account account);
    boolean transferFunds(String fromAccountNumber, String toAccountNumber, double amount);
    boolean deposit(String accountNumber, double amount);
    boolean withdraw(String accountNumber, double amount);
    boolean applyInterest(String accountNumber);
    void showCustomerStatement(String customerId);
    void showAccountStatement(String accountNumber);
    String getName();
}