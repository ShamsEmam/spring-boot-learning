package repository.jdbc;

import model.Account;
import model.CurrentAccount;
import model.Customer;
import model.SavingsAccount;
import repository.AccountRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcAccountRepository implements AccountRepository {
    private final String url;
    private final String user;
    private final String password;

    public JdbcAccountRepository(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
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

                if ("SAVINGS".equals(type)) {
                    double rate = rs.getDouble("interest_rate");
                    return new SavingsAccount(accountNumber, balance, customer, rate);
                } else if ("CURRENT".equals(type)) {
                    double overdraft = rs.getDouble("overdraft_limit");
                    return new CurrentAccount(accountNumber, balance, customer, overdraft);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
    public boolean save(Account account) {
        if (account == null) return false;

        String sql = "INSERT INTO accounts (account_number, balance, account_type, interest_rate, overdraft_limit, customer_id) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (account_number) DO UPDATE SET balance = EXCLUDED.balance";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, account.getAccountNumber());
            stmt.setDouble(2, account.getBalance());

            if (account instanceof SavingsAccount savings) {
                stmt.setString(3, "SAVINGS");
                stmt.setDouble(4, savings.getInterestRate());
                stmt.setDouble(5, 0.0);
            } else if (account instanceof CurrentAccount current) {
                stmt.setString(3, "CURRENT");
                stmt.setDouble(4, 0.0);
                stmt.setDouble(5, current.getOverdraftLimit());
            }

            stmt.setString(6, account.getAccountHolder().getCustomerId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
}