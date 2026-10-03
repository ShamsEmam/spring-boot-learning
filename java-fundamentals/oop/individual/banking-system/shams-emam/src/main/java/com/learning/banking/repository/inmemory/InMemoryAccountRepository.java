package com.learning.banking.repository.inmemory;

import com.learning.banking.model.Account;
import com.learning.banking.repository.AccountRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryAccountRepository
        implements AccountRepository {

    private final List<Account> accounts =
            new ArrayList<>();

    @Override
    public void save(Account account) {

        if (account == null) {
            throw new IllegalArgumentException(
                    "Account cannot be null"
            );
        }

        if (existsById(account.getAccountId())) {
            throw new IllegalArgumentException(
                    "Account ID already exists"
            );
        }

        accounts.add(account);
    }

    @Override
    public Account findById(String accountId) {

        validateId(accountId);

        for (Account account : accounts) {
            if (account.getAccountId().equals(accountId)) {
                return account;
            }
        }

        return null;
    }

    @Override
    public boolean existsById(String accountId) {
        return findById(accountId) != null;
    }

    @Override
    public List<Account> findAll() {
        return List.copyOf(accounts);
    }

    private void validateId(String accountId) {

        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException(
                    "Account ID cannot be null or blank"
            );
        }
    }
}