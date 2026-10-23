package com.learning.banking.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.UnaryOperator;

/**
 * The three values needed to reach the database.
 *
 * Single responsibility: know WHERE the settings come from.
 * Environment variables win; db.properties is the fallback.
 */
public record DatabaseSettings(String url, String user, String password) {

    private static final String PROPERTIES_FILE = "db.properties";

    public static DatabaseSettings load() {
        return from(loadPropertiesFile(), System::getenv);
    }

    /** Separate from load() so tests can pass fake properties and a fake environment. */
    static DatabaseSettings from(
            Properties properties,
            UnaryOperator<String> environment) {

        return new DatabaseSettings(
                read("DB_URL", "db.url", properties, environment),
                read("DB_USER", "db.user", properties, environment),
                read("DB_PASSWORD", "db.password", properties, environment)
        );
    }

    private static String read(
            String environmentName,
            String propertyName,
            Properties properties,
            UnaryOperator<String> environment) {

        String value = environment.apply(environmentName);

        if (value == null || value.isBlank()) {
            value = properties.getProperty(propertyName);
        }

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing database setting: set " + environmentName
                            + " or " + propertyName + " in " + PROPERTIES_FILE
            );
        }

        return value;
    }

    private static Properties loadPropertiesFile() {
        Properties properties = new Properties();

        try (InputStream input = DatabaseSettings.class
                .getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {

            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not read " + PROPERTIES_FILE, e);
        }

        return properties;
    }
}
