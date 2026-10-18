package factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionFactory {

    private static volatile DatabaseConnectionFactory instance;

    private final String url = "jdbc:postgresql://localhost:5432/bank_db";
    private final String user = "postgres";
    private final String password = "12345";

    private DatabaseConnectionFactory() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseConnectionFactory getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnectionFactory.class) {
                if (instance == null) {
                    instance = new DatabaseConnectionFactory();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}