package service;

import model.Account;

public interface AccountService {
    boolean deposit(String accountNumber, double amount);
    boolean withdraw(String accountNumber, double amount);
    boolean applyInterest(String accountNumber);
    double getBalance(String accountNumber);
    Account getAccount(String accountNumber);
}