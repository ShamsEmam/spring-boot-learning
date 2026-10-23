package com.learning.banking.service.impl;

import com.learning.banking.model.Account;
import com.learning.banking.repository.AccountRepository;
import com.learning.banking.service.TransferService;

import java.util.List;

public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;

    public TransferServiceImpl(
            AccountRepository accountRepository) {

        if (accountRepository == null) {
            throw new IllegalArgumentException(
                    "Account repository cannot be null"
            );
        }

        this.accountRepository = accountRepository;
    }

    @Override
    public void transfer(
            String sourceAccountId,
            String destinationAccountId,
            double amount) {

        validateDifferentAccounts(sourceAccountId, destinationAccountId);
        validateAmount(amount);

        Account source =
                AccountLookup.findExisting(accountRepository, sourceAccountId);

        Account destination =
                AccountLookup.findExisting(accountRepository, destinationAccountId);

        source.withdraw(amount);
        destination.deposit(amount);

        // ONE atomic save: both balances are stored together or not at all.
        accountRepository.saveAll(List.of(source, destination));
    }

    private void validateDifferentAccounts(
            String sourceAccountId,
            String destinationAccountId) {

        if (sourceAccountId == null
                || destinationAccountId == null) {

            throw new IllegalArgumentException(
                    "Account IDs cannot be null"
            );
        }

        if (sourceAccountId.equals(destinationAccountId)) {
            throw new IllegalArgumentException(
                    "Source and destination accounts cannot be the same"
            );
        }
    }

    private void validateAmount(double amount) {

        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException(
                    "Transfer amount must be a valid number greater than zero"
            );
        }
    }
}
