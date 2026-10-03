package com.learning.banking.service.impl;

import com.learning.banking.model.Account;
import com.learning.banking.repository.AccountRepository;
import com.learning.banking.repository.CustomerRepository;
import com.learning.banking.service.AccountService;

public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            CustomerRepository customerRepository) {

        if (accountRepository == null) {
            throw new IllegalArgumentException(
                    "Account repository cannot be null"
            );
        }

        if (customerRepository == null) {
            throw new IllegalArgumentException(
                    "Customer repository cannot be null"
            );
        }

        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void openAccount(Account account) {

        if (account == null) {
            throw new IllegalArgumentException(
                    "Account cannot be null"
            );
        }

        String customerId =
                account.getOwner().getCustomerId();

        if (!customerRepository.existsById(customerId)) {
            throw new IllegalStateException(
                    "Account owner must be registered first"
            );
        }

        if (accountRepository.existsById(
                account.getAccountId())) {

            throw new IllegalArgumentException(
                    "Account ID already exists"
            );
        }

        accountRepository.save(account);
    }

    @Override
    public void deposit(
            String accountId,
            double amount) {

        Account account =
                getExistingAccount(accountId);

        account.deposit(amount);
    }

    @Override
    public double withdraw(
            String accountId,
            double amount) {

        Account account =
                getExistingAccount(accountId);

        return account.withdraw(amount);
    }

    @Override
    public Account findAccountById(String accountId) {

        return getExistingAccount(accountId);
    }

    private Account getExistingAccount(String accountId) {

        Account account =
                accountRepository.findById(accountId);

        if (account == null) {
            throw new IllegalArgumentException(
                    "Account not found: " + accountId
            );
        }

        return account;
    }
}