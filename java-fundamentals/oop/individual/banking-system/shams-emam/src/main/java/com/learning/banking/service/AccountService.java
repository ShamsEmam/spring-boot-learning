package com.learning.banking.service;

import com.learning.banking.model.Account;

public interface AccountService {

    void openAccount(Account account);

    void deposit(String accountId, double amount);

    double withdraw(String accountId, double amount);

    Account findAccountById(String accountId);
}