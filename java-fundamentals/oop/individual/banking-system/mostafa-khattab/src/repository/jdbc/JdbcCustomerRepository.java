package repository.jdbc;

import model.Account;
import model.Customer;
import repository.AccountRepository;
import repository.CustomerRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcCustomerRepository implements CustomerRepository {
    private final String url;
    private final String user;
    private final String password;
    private final AccountRepository accountRepository;

    public JdbcCustomerRepository(String url, String user, String password, AccountRepository accountRepository) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.accountRepository = accountRepository;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public boolean save(Customer customer) {
        if (customer == null) return false;

        String sql = "INSERT INTO customers (customer_id, name) VALUES (?, ?) " +
                "ON CONFLICT (customer_id) DO UPDATE SET name = EXCLUDED.name";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getCustomerId());
            stmt.setString(2, customer.getName());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Customer findById(String customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer(rs.getString("customer_id"), rs.getString("name"));

                // استخدام internalAddAccount بدلاً من addAccount
                List<Account> customerAccounts = accountRepository.findByCustomerId(customerId);
                for (Account acc : customerAccounts) {
                    customer.internalAddAccount(acc);
                }

                return customer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean exists(String customerId) {
        return findById(customerId) != null;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Customer customer = findById(rs.getString("customer_id"));
                if (customer != null) {
                    customers.add(customer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }
}