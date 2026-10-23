package com.learning.banking.repository.mysql;

import com.learning.banking.config.ConnectionProvider;
import com.learning.banking.model.Branch;
import com.learning.banking.repository.BranchRepository;
import com.learning.banking.repository.DataAccessException;
import com.learning.banking.util.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlBranchRepository implements BranchRepository {

    private static final String UPSERT = """
            INSERT INTO branches (branch_id, name, address, phone, email, manager)
            VALUES (?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                name = VALUES(name),
                address = VALUES(address),
                phone = VALUES(phone),
                email = VALUES(email),
                manager = VALUES(manager)
            """;

    private static final String SELECT =
            "SELECT branch_id, name, address, phone, email, manager FROM branches";

    private final ConnectionProvider connectionProvider;

    public MySqlBranchRepository(ConnectionProvider connectionProvider) {
        if (connectionProvider == null) {
            throw new IllegalArgumentException(
                    "Connection provider cannot be null"
            );
        }
        this.connectionProvider = connectionProvider;
    }

    @Override
    public void save(Branch branch) {
        if (branch == null) {
            throw new IllegalArgumentException("Branch cannot be null");
        }

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPSERT)) {

            statement.setString(1, branch.getBranchId());
            statement.setString(2, branch.getName());
            statement.setString(3, branch.getAddress());
            statement.setString(4, branch.getPhone());
            statement.setString(5, branch.getEmail());
            statement.setString(6, branch.getManager());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Could not save branch " + branch.getBranchId(), e);
        }
    }

    @Override
    public Branch findById(String branchId) {
        Validator.requireNotBlank(branchId, "Branch ID");

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     SELECT + " WHERE branch_id = ?")) {

            statement.setString(1, branchId);

            try (ResultSet row = statement.executeQuery()) {
                return row.next() ? toBranch(row) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Could not read branch " + branchId, e);
        }
    }

    @Override
    public boolean existsById(String branchId) {
        Validator.requireNotBlank(branchId, "Branch ID");

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT 1 FROM branches WHERE branch_id = ?")) {

            statement.setString(1, branchId);

            try (ResultSet row = statement.executeQuery()) {
                return row.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Could not check branch " + branchId, e);
        }
    }

    @Override
    public List<Branch> findAll() {
        List<Branch> branches = new ArrayList<>();

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     SELECT + " ORDER BY branch_id");
             ResultSet row = statement.executeQuery()) {

            while (row.next()) {
                branches.add(toBranch(row));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not read branches", e);
        }

        return List.copyOf(branches);
    }

    private Branch toBranch(ResultSet row) throws SQLException {
        return new Branch(
                row.getString("branch_id"),
                row.getString("name"),
                row.getString("address"),
                row.getString("phone"),
                row.getString("email"),
                row.getString("manager")
        );
    }
}
