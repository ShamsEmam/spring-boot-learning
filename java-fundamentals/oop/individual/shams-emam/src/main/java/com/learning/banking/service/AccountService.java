package com.learning.banking.service;

import com.learning.banking.model.Account;

public interface AccountService {

    void openAccount(Account account);

    void deposit(String accountId, double amount);

    double withdraw(String accountId, double amount);

    Account findAccountById(String accountId);

    /**
     * Closes an account (soft delete): it stops being visible and usable,
     * but every row of its data stays in the database.
     * The balance must be zero first, so no money is ever left behind.
     */
    void closeAccount(String accountId);
}
