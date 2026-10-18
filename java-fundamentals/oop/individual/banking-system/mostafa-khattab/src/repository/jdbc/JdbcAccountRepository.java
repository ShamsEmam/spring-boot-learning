package repository.jdbc;

import factory.AccountFactory;
import factory.DatabaseConnectionFactory;
import model.Account;
import model.Customer;
import repository.AccountRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcAccountRepository implements AccountRepository {

    public JdbcAccountRepository() {
    }

    private Connection getConnection() throws SQLException {
        return DatabaseConnectionFactory.getInstance().getConnection();
    }

    private String fetchCustomerName(Connection conn, String customerId) {
        String sql = "SELECT name FROM customers WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

    @Override
    public Account findByNumber(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String type = rs.getString("account_type");
                double balance = rs.getDouble("balance");
                String customerId = rs.getString("customer_id");
                String customerName = fetchCustomerName(conn, customerId);

                Customer customer = new Customer(customerId, customerName);

                if ("SAVING".equalsIgnoreCase(type) || "SAVINGS".equalsIgnoreCase(type)) {
                    double rate = rs.getDouble("interest_rate");
                    return AccountFactory.createAccount(Account.AccountType.SAVING, accountNumber, balance, customer, rate);
                } else if ("CURRENT".equalsIgnoreCase(type)) {
                    double overdraft = rs.getDouble("overdraft_limit");
                    return AccountFactory.createAccount(Account.AccountType.CURRENT, accountNumber, balance, customer, overdraft);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean save(Account account) {
        if (account == null) return false;

        String sql = "INSERT INTO accounts (account_number, balance, account_type, interest_rate, overdraft_limit, customer_id) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (account_number) DO UPDATE SET balance = EXCLUDED.balance";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, account.getAccountNumber());
            stmt.setDouble(2, account.getBalance());

            if (account.getAccountType() == Account.AccountType.SAVING) {
                stmt.setString(3, "SAVING");
                stmt.setDouble(4, account.getInterestRate());
                stmt.setDouble(5, 0.0);
            } else if (account.getAccountType() == Account.AccountType.CURRENT) {
                stmt.setString(3, "CURRENT");
                stmt.setDouble(4, 0.0);
                stmt.setDouble(5, account.getOverdraftLimit());
            }

            stmt.setString(6, account.getAccountHolder().getCustomerId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Account> findByCustomerId(String customerId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_number FROM accounts WHERE customer_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Account acc = findByNumber(rs.getString("account_number"));
                if (acc != null) {
                    accounts.add(acc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public boolean exists(String accountNumber) {
        return findByNumber(accountNumber) != null;
    }

    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_number FROM accounts";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Account acc = findByNumber(rs.getString("account_number"));
                if (acc != null) accounts.add(acc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }


    @Override
    public boolean transferFunds(
            String fromAccountNumber,
            String toAccountNumber,
            double amount) {

        if (fromAccountNumber == null
                || toAccountNumber == null
                || fromAccountNumber.equals(toAccountNumber)
                || !Double.isFinite(amount)
                || amount <= 0) {
            return false;
        }

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try {
                Double fromBalance = null;
                Double toBalance = null;
                String fromType = null;
                Double overdraftLimit = null;
                String fromCustomerId = null;
                String fromCustomerName = null;
                double fromInterestRate = 0.0;

                String selectSql =
                        "SELECT a.account_number, a.balance, a.account_type, "
                                + "a.overdraft_limit, a.interest_rate, a.customer_id, "
                                + "c.name AS customer_name "
                                + "FROM accounts a "
                                + "JOIN customers c ON c.customer_id = a.customer_id "
                                + "WHERE a.account_number IN (?, ?) "
                                + "ORDER BY a.account_number FOR UPDATE OF a";

                try (PreparedStatement stmt =
                             conn.prepareStatement(selectSql)) {

                    stmt.setString(1, fromAccountNumber);
                    stmt.setString(2, toAccountNumber);

                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            String number = rs.getString("account_number");
                            double balance = rs.getDouble("balance");

                            if (number.equals(fromAccountNumber)) {
                                fromBalance = balance;
                                fromType = rs.getString("account_type");
                                overdraftLimit =
                                        rs.getDouble("overdraft_limit");
                                fromCustomerId = rs.getString("customer_id");
                                fromCustomerName = rs.getString("customer_name");
                                fromInterestRate = rs.getDouble("interest_rate");

                            } else if (number.equals(toAccountNumber)) {
                                toBalance = balance;
                            }
                        }
                    }
                }

                if (fromBalance == null || toBalance == null) {
                    conn.rollback();
                    return false;
                }

                if (fromCustomerId == null
                        || fromCustomerName == null
                        || fromCustomerName.trim().isEmpty()) {
                    conn.rollback();
                    return false;
                }

                Account fromAccount;

                Customer fromCustomer =
                        new Customer(fromCustomerId, fromCustomerName);

                if ("SAVING".equalsIgnoreCase(fromType)
                        || "SAVINGS".equalsIgnoreCase(fromType)) {

                    fromAccount = AccountFactory.createAccount(
                            Account.AccountType.SAVING,
                            fromAccountNumber,
                            fromBalance,
                            fromCustomer,
                            fromInterestRate
                    );

                } else if ("CURRENT".equalsIgnoreCase(fromType)) {

                    fromAccount = AccountFactory.createAccount(
                            Account.AccountType.CURRENT,
                            fromAccountNumber,
                            fromBalance,
                            fromCustomer,
                            overdraftLimit
                    );

                } else {
                    conn.rollback();
                    return false;
                }

                // Use Strategy Pattern to validate withdrawal.
                if (!fromAccount.canWithdraw(amount)) {
                    conn.rollback();
                    return false;
                }

                double newFromBalance = fromBalance - amount;
                double newToBalance = toBalance + amount;

                if (!Double.isFinite(newFromBalance)
                        || !Double.isFinite(newToBalance)) {
                    conn.rollback();
                    return false;
                }

                String updateSql =
                        "UPDATE accounts SET balance = ? "
                                + "WHERE account_number = ?";

                try (PreparedStatement stmt =
                             conn.prepareStatement(updateSql)) {

                    stmt.setDouble(1, newFromBalance);
                    stmt.setString(2, fromAccountNumber);

                    if (stmt.executeUpdate() != 1) {
                        conn.rollback();
                        return false;
                    }

                    stmt.setDouble(1, newToBalance);
                    stmt.setString(2, toAccountNumber);

                    if (stmt.executeUpdate() != 1) {
                        conn.rollback();
                        return false;
                    }
                }

                conn.commit();
                return true;

            } catch (SQLException | RuntimeException e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}