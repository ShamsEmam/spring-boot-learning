package com.learning.banking.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @Test
    void shouldAcceptNormalText() {
        assertDoesNotThrow(() -> Validator.requireNotBlank("abc", "Name"));
    }

    @Test
    void shouldRejectNullWithFieldNameInMessage() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Validator.requireNotBlank(null, "Name")
        );

        assertEquals("Name cannot be null or blank", exception.getMessage());
    }

    @Test
    void shouldRejectBlankText() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Validator.requireNotBlank("   ", "Name")
        );
    }
}
