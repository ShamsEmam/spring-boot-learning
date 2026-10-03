package repository.jdbc;

import model.Lookup;
import repository.LookupRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcLookupRepository implements LookupRepository {
    private final String url;
    private final String user;
    private final String password;

    public JdbcLookupRepository(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public List<Lookup> findActiveByCategory(String categoryCode) {
        List<Lookup> lookups = new ArrayList<>();
        String sql = "SELECT * FROM lookups WHERE category_code = ? AND is_active = true";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoryCode);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lookups.add(new Lookup(
                        rs.getInt("id"),
                        rs.getString("category_code"),
                        rs.getString("item_code"),
                        rs.getString("item_value"),
                        rs.getString("item_name"),
                        rs.getBoolean("is_active")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lookups;
    }

    @Override
    public boolean softDelete(String categoryCode, String itemCode) {
        String sql = "UPDATE lookups SET is_active = false WHERE category_code = ? AND item_code = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoryCode);
            stmt.setString(2, itemCode);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean existsActive(String categoryCode, String itemCode) {
        String sql = "SELECT 1 FROM lookups WHERE category_code = ? AND item_code = ? AND is_active = true";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoryCode);
            stmt.setString(2, itemCode);
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}