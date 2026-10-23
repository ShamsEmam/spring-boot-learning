package com.learning.banking.repository.mysql;

import com.learning.banking.config.ConnectionProvider;
import com.learning.banking.model.Account;
import com.learning.banking.repository.AccountRepository;
import com.learning.banking.repository.DataAccessException;
import com.learning.banking.util.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlAccountRepository implements AccountRepository {

    /** Reads an account together with its owner in ONE query (no N+1 lookups). */
    private static final String SELECT_ACTIVE_WITH_OWNER = """
            SELECT a.account_id, a.account_type, a.balance,
                   a.interest_rate, a.minimum_balance,
                   a.overdraft_limit, a.withdrawal_limit,
                   c.customer_id,
                   c.name     AS customer_name,
                   c.email    AS customer_email,
                   c.password AS customer_password
              FROM accounts a
              JOIN customers c ON c.customer_id = a.customer_id
             WHERE a.is_active = TRUE
            """;

    private static final String SELECT_STATE =
            "SELECT is_active FROM accounts WHERE account_id = ? FOR UPDATE";

    private static final String INSERT = """
            INSERT INTO accounts
                   (account_type, balance, customer_id, interest_rate,
                    minimum_balance, overdraft_limit, withdrawal_limit,
                    account_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String UPDATE = """
            UPDATE accounts
               SET account_type = ?, balance = ?, customer_id = ?,
                   interest_rate = ?, minimum_balance = ?,
                   overdraft_limit = ?, withdrawal_limit = ?
             WHERE account_id = ?
            """;

    private static final String DEACTIVATE = """
            UPDATE accounts
               SET is_active = FALSE, deactivated_at = CURRENT_TIMESTAMP
             WHERE account_id = ? AND is_active = TRUE
            """;

    private enum RowState { MISSING, ACTIVE, INACTIVE }

    private final ConnectionProvider connectionProvider;

    public MySqlAccountRepository(ConnectionProvider connectionProvider) {
        if (connectionProvider == null) {
            throw new IllegalArgumentException(
                    "Connection provider cannot be null"
            );
        }
        this.connectionProvider = connectionProvider;
    }

    @Override
    public void save(Account account) {
        requireAccount(account);

        try (Connection connection = connectionProvider.getConnection()) {
            saveUsing(connection, account);
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Could not save account " + account.getAccountId(), e);
        }
    }

    /**
     * One TRANSACTION: all the accounts are saved together, or (if anything
     * fails) none of them is. A money transfer relies on this so that money
     * can never leave one account without arriving in the other.
     */
    @Override
    public void saveAll(List<Account> accounts) {
        if (accounts == null) {
            throw new IllegalArgumentException("Accounts cannot be null");
        }

        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);

            try {
                for (Account account : accounts) {
                    requireAccount(account);
                    saveUsing(connection, account);
                }
                connection.commit();
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not save accounts", e);
        }
    }

    @Override
    public Account findById(String accountId) {
        Validator.requireNotBlank(accountId, "Account ID");

        String sql = SELECT_ACTIVE_WITH_OWNER + " AND a.account_id = ?";

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, accountId);

            try (ResultSet row = statement.executeQuery()) {
                return row.next() ? AccountMapper.toAccount(row) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Could not read account " + accountId, e);
        }
    }

    @Override
    public boolean existsById(String accountId) {
        return exists(accountId,
                "SELECT 1 FROM accounts WHERE account_id = ? AND is_active = TRUE");
    }

    @Override
    public boolean existsByIdIncludingInactive(String accountId) {
        return exists(accountId,
                "SELECT 1 FROM accounts WHERE account_id = ?");
    }

    @Override
    public List<Account> findAll() {
        String sql = SELECT_ACTIVE_WITH_OWNER + " ORDER BY a.account_id";
        List<Account> accounts = new ArrayList<>();

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet row = statement.executeQuery()) {

            while (row.next()) {
                accounts.add(AccountMapper.toAccount(row));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not read accounts", e);
        }

        return List.copyOf(accounts);
    }

    @Override
    public void deactivateById(String accountId) {
        Validator.requireNotBlank(accountId, "Account ID");

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(DEACTIVATE)) {

            statement.setString(1, accountId);

            if (statement.executeUpdate() == 0) {
                throw new IllegalArgumentException(
                        "Active account not found: " + accountId
                );
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Could not deactivate account " + accountId, e);
        }
    }

    // ------------------------------------------------------------------

    /** Insert if new, update if active, refuse if closed. */
    private void saveUsing(Connection connection, Account account)
            throws SQLException {

        switch (findState(connection, account.getAccountId())) {
            case MISSING -> write(connection, INSERT, account);
            case ACTIVE -> write(connection, UPDATE, account);
            case INACTIVE -> throw new IllegalStateException(
                    "Account is closed and cannot be modified: "
                            + account.getAccountId());
        }
    }

    private RowState findState(Connection connection, String accountId)
            throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(SELECT_STATE)) {
            statement.setString(1, accountId);

            try (ResultSet row = statement.executeQuery()) {
                if (!row.next()) {
                    return RowState.MISSING;
                }
                return row.getBoolean("is_active")
                        ? RowState.ACTIVE : RowState.INACTIVE;
            }
        }
    }

    private void write(Connection connection, String sql, Account account)
            throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            AccountMapper.bindColumns(statement, account);
            statement.setString(8, account.getAccountId());
            statement.executeUpdate();
        }
    }

    private boolean exists(String accountId, String sql) {
        Validator.requireNotBlank(accountId, "Account ID");

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, accountId);

            try (ResultSet row = statement.executeQuery()) {
                return row.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Could not check account " + accountId, e);
        }
    }

    private void requireAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
    }
}
