package com.learning.banking.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * SINGLETON: the whole application shares exactly one DatabaseConnection
 * object, which owns the database settings.
 *
 * How the pattern is built here:
 *  1. private constructor      -> nobody else can call "new"
 *  2. private static INSTANCE  -> the one shared object
 *  3. public getInstance()     -> the only way to get it
 *  4. volatile + double-checked locking -> safe when many threads call
 *     getInstance() at the same time, and still fast after the first call
 *
 * Settings are read lazily (on the first getConnection), so getInstance()
 * itself never fails, and a missing setting gives a clear error message.
 *
 * Note: only the SETTINGS are shared. Each call to getConnection() still
 * opens a fresh Connection, because a single shared Connection would not be
 * safe for several threads or for transactions.
 */
public final class DatabaseConnection implements ConnectionProvider {

    private static volatile DatabaseConnection instance;

    private DatabaseSettings settings;

    private DatabaseConnection() {
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {                          // 1st check: no lock (fast)
            synchronized (DatabaseConnection.class) {
                if (instance == null) {                  // 2nd check: with lock (safe)
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        DatabaseSettings currentSettings = settings();

        return DriverManager.getConnection(
                currentSettings.url(),
                currentSettings.user(),
                currentSettings.password()
        );
    }

    private synchronized DatabaseSettings settings() {
        if (settings == null) {
            settings = DatabaseSettings.load();
        }
        return settings;
    }
}
