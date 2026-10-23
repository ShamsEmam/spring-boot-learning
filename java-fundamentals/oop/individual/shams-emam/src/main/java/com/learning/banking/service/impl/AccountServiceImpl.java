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

        String customerId = account.getOwner().getCustomerId();

        if (!customerRepository.existsById(customerId)) {
            throw new IllegalStateException(
                    "Account owner must be registered first"
            );
        }

        // Includes closed accounts: an ID is never reused, otherwise the new
        // account could overwrite the history of the closed one.
        if (accountRepository.existsByIdIncludingInactive(
                account.getAccountId())) {

            throw new IllegalArgumentException(
                    "Account ID already exists"
            );
        }

        accountRepository.save(account);
    }

    @Override
    public void deposit(String accountId, double amount) {

        Account account = findActiveAccount(accountId);

        account.deposit(amount);

        accountRepository.save(account);
    }

    @Override
    public double withdraw(String accountId, double amount) {

        Account account = findActiveAccount(accountId);

        double withdrawnAmount = account.withdraw(amount);

        accountRepository.save(account);

        return withdrawnAmount;
    }

    @Override
    public Account findAccountById(String accountId) {
        return findActiveAccount(accountId);
    }

    @Override
    public void closeAccount(String accountId) {

        Account account = findActiveAccount(accountId);

        if (account.getBalance() != 0) {
            throw new IllegalStateException(
                    "Account balance must be zero before the account can be closed"
            );
        }

        accountRepository.deactivateById(accountId);
    }

    private Account findActiveAccount(String accountId) {
        return AccountLookup.findExisting(accountRepository, accountId);
    }
}
