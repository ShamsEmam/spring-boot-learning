package com.learning.banking.repository;

import com.learning.banking.model.Account;

import java.util.List;

/**
 * Storage for accounts.
 *
 * Accounts are never physically deleted (soft delete): closing an account
 * marks it inactive and keeps every row. Unless a method says otherwise,
 * it only sees ACTIVE accounts.
 *
 * Every implementation (in-memory, MySQL...) must behave the same way
 * (Liskov Substitution Principle). Business rules such as "no duplicate IDs"
 * live in the service layer, not here.
 */
public interface AccountRepository {

    /**
     * Inserts a new account or updates an existing active one.
     *
     * @throws IllegalStateException if the account was closed (inactive):
     *         a closed account is history and must never be overwritten
     */
    void save(Account account);

    /** Saves all accounts together: either every one is saved or none is. */
    void saveAll(List<Account> accounts);

    /** The ACTIVE account with this ID, or null. */
    Account findById(String accountId);

    /** True if an ACTIVE account has this ID. */
    boolean existsById(String accountId);

    /** True if any account, active or closed, has this ID (IDs are never reused). */
    boolean existsByIdIncludingInactive(String accountId);

    /** All ACTIVE accounts. */
    List<Account> findAll();

    /**
     * Soft delete: marks the account inactive. The row and its data are kept.
     *
     * @throws IllegalArgumentException if there is no active account with this ID
     */
    void deactivateById(String accountId);
}
