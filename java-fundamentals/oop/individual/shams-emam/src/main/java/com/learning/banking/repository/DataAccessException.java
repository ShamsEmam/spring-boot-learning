package com.learning.banking.repository;

/**
 * Thrown when the storage layer fails (for example a SQL error).
 * It replaces the bare RuntimeException the old code threw, so callers
 * can tell "the database failed" apart from "your input was invalid".
 */
public class DataAccessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
