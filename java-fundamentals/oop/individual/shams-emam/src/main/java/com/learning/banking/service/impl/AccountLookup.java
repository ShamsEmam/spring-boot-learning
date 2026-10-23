package com.learning.banking.service.impl;

import com.learning.banking.model.Account;
import com.learning.banking.repository.AccountRepository;

/**
 * "Find the account or fail" used to be copy-pasted in two services.
 * It now lives in one place (DRY).
 */
final class AccountLookup {

    private AccountLookup() {
    }

    static Account findExisting(
            AccountRepository accountRepository,
            String accountId) {

        Account account = accountRepository.findById(accountId);

        if (account == null) {
            throw new IllegalArgumentException(
                    "Account not found: " + accountId
            );
        }

        return account;
    }
}
