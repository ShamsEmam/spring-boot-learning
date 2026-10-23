package com.learning.banking.model;

/**
 * The kinds of account the bank offers.
 * Stored in the database as the enum name, e.g. "SAVINGS".
 */
public enum AccountType {
    STANDARD,
    SAVINGS,
    CURRENT,
    LIMITED
}
