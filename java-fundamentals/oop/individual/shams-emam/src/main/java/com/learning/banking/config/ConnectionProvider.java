package com.learning.banking.config;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Anything that can hand out database connections.
 *
 * Repositories depend on THIS interface, not on a concrete class
 * (Dependency Inversion Principle). That lets us swap the real database
 * for a test database without touching any repository code.
 */
public interface ConnectionProvider {

    Connection getConnection() throws SQLException;
}
