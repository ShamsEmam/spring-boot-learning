package com.learning.banking.service.impl;

import com.learning.banking.model.Account;
import com.learning.banking.repository.AccountRepository;
import com.learning.banking.service.TransferService;

public class BankTransferService implements TransferService {

    private final AccountRepository accountRepository;

    public BankTransferService(
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

        validateDifferentAccounts(
                sourceAccountId,
                destinationAccountId
        );

        validateAmount(amount);

        Account source =
                getExistingAccount(sourceAccountId);

        Account destination =
                getExistingAccount(destinationAccountId);

        source.withdraw(amount);
        destination.deposit(amount);
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