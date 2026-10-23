package com.learning.banking.repository.inmemory;

import com.learning.banking.model.Account;
import com.learning.banking.repository.AccountRepository;
import com.learning.banking.util.Validator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InMemoryAccountRepository implements AccountRepository {

    private final List<Account> accounts = new ArrayList<>();
    private final Set<String> inactiveAccountIds = new HashSet<>();

    @Override
    public void save(Account account) {
        validateAccount(account);
        store(account);
    }

    @Override
    public void saveAll(List<Account> accountsToSave) {
        if (accountsToSave == null) {
            throw new IllegalArgumentException("Accounts cannot be null");
        }

        // Check everything first so that a bad item saves nothing (all or none).
        for (Account account : accountsToSave) {
            validateAccount(account);
        }

        for (Account account : accountsToSave) {
            store(account);
        }
    }

    @Override
    public Account findById(String accountId) {
        Validator.requireNotBlank(accountId, "Account ID");

        if (inactiveAccountIds.contains(accountId)) {
            return null;
        }

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
    public boolean existsByIdIncludingInactive(String accountId) {
        Validator.requireNotBlank(accountId, "Account ID");

        for (Account account : accounts) {
            if (account.getAccountId().equals(accountId)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<Account> findAll() {
        return accounts.stream()
                .filter(account -> !inactiveAccountIds.contains(account.getAccountId()))
                .toList();
    }

    @Override
    public void deactivateById(String accountId) {
        if (!existsById(accountId)) {
            throw new IllegalArgumentException(
                    "Active account not found: " + accountId
            );
        }

        inactiveAccountIds.add(accountId);
    }

    private void store(Account account) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountId().equals(account.getAccountId())) {
                accounts.set(i, account);
                return;
            }
        }

        accounts.add(account);
    }

    private void validateAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }

        if (inactiveAccountIds.contains(account.getAccountId())) {
            throw new IllegalStateException(
                    "Account is closed and cannot be modified: "
                            + account.getAccountId()
            );
        }
    }
}
