package com.learning.banking.util;

/**
 * One home for the "required text" rule that used to be copy-pasted
 * in Bank, Branch, Customer and the repositories (DRY).
 */
public final class Validator {

    private Validator() {
    }

    public static void requireNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be null or blank"
            );
        }
    }
}
