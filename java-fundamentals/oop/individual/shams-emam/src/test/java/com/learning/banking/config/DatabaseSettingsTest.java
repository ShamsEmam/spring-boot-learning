package com.learning.banking.config;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseSettingsTest {

    private static Properties fileProperties() {
        Properties properties = new Properties();
        properties.setProperty("db.url", "jdbc:file");
        properties.setProperty("db.user", "file-user");
        properties.setProperty("db.password", "file-password");
        return properties;
    }

    @Test
    void shouldReadValuesFromPropertiesWhenEnvironmentIsEmpty() {

        DatabaseSettings settings =
                DatabaseSettings.from(fileProperties(), name -> null);

        assertEquals("jdbc:file", settings.url());
        assertEquals("file-user", settings.user());
        assertEquals("file-password", settings.password());
    }

    @Test
    void environmentShouldWinOverProperties() {

        Map<String, String> environment = Map.of("DB_USER", "env-user");

        DatabaseSettings settings =
                DatabaseSettings.from(fileProperties(), environment::get);

        assertEquals("env-user", settings.user());
        assertEquals("jdbc:file", settings.url());
    }

    @Test
    void shouldFailWithClearMessageWhenSettingIsMissing() {

        Properties properties = fileProperties();
        properties.remove("db.password");

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> DatabaseSettings.from(properties, name -> null)
        );

        assertTrue(exception.getMessage().contains("DB_PASSWORD"));
    }

    @Test
    void shouldTreatBlankValueAsMissing() {

        Properties properties = fileProperties();
        properties.setProperty("db.url", "   ");

        assertThrows(
                IllegalStateException.class,
                () -> DatabaseSettings.from(properties, name -> null)
        );
    }
}
