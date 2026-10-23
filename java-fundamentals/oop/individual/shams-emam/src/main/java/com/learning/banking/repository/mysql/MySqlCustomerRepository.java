package com.learning.banking.repository.mysql;

import com.learning.banking.config.ConnectionProvider;
import com.learning.banking.model.Customer;
import com.learning.banking.repository.CustomerRepository;
import com.learning.banking.repository.DataAccessException;
import com.learning.banking.util.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlCustomerRepository implements CustomerRepository {

    private static final String UPSERT = """
            INSERT INTO customers (customer_id, name, email, password)
            VALUES (?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                name = VALUES(name),
                email = VALUES(email),
                password = VALUES(password)
            """;

    private static final String SELECT =
            "SELECT customer_id, name, email, password FROM customers";

    private final ConnectionProvider connectionProvider;

    public MySqlCustomerRepository(ConnectionProvider connectionProvider) {
        if (connectionProvider == null) {
            throw new IllegalArgumentException(
                    "Connection provider cannot be null"
            );
        }
        this.connectionProvider = connectionProvider;
    }

    @Override
    public void save(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPSERT)) {

            statement.setString(1, customer.getCustomerId());
            statement.setString(2, customer.getName());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Could not save customer " + customer.getCustomerId(), e);
        }
    }

    @Override
    public Customer findById(String customerId) {
        Validator.requireNotBlank(customerId, "Customer ID");

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     SELECT + " WHERE customer_id = ?")) {

            statement.setString(1, customerId);

            try (ResultSet row = statement.executeQuery()) {
                return row.next() ? toCustomer(row) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Could not read customer " + customerId, e);
        }
    }

    @Override
    public boolean existsById(String customerId) {
        Validator.requireNotBlank(customerId, "Customer ID");

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT 1 FROM customers WHERE customer_id = ?")) {

            statement.setString(1, customerId);

            try (ResultSet row = statement.executeQuery()) {
                return row.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Could not check customer " + customerId, e);
        }
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     SELECT + " ORDER BY customer_id");
             ResultSet row = statement.executeQuery()) {

            while (row.next()) {
                customers.add(toCustomer(row));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not read customers", e);
        }

        return List.copyOf(customers);
    }

    private Customer toCustomer(ResultSet row) throws SQLException {
        return new Customer(
                row.getString("customer_id"),
                row.getString("name"),
                row.getString("email"),
                row.getString("password")
        );
    }
}
