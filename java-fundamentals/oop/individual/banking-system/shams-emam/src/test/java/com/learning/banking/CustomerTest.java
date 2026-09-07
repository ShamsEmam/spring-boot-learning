package com.learning.banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(
                "CUST001",
                "Shams",
                "shams@gmail.com",
                "123456"
        );
    }

    // ============Constructor Tests=============

    @Test
    void constructorShouldCreateCustomerSuccessfully() {

        assertEquals("CUST001", customer.getCustomerId());
        assertEquals("Shams", customer.getName());
        assertEquals("shams@gmail.com", customer.getEmail());
    }

    @Test
    void constructorShouldRejectNullCustomerId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Customer(
                        null,
                        "Shams",
                        "shams@gmail.com",
                        "123456"
                )
        );
    }

    @Test
    void constructorShouldRejectBlankCustomerId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Customer(
                        "   ",
                        "Shams",
                        "shams@gmail.com",
                        "123456"
                )
        );
    }

    @Test
    void constructorShouldRejectCustomerIdContainingSpaces() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Customer(
                        "CUST 001",
                        "Shams",
                        "shams@gmail.com",
                        "123456"
                )
        );
    }

    @Test
    void constructorShouldRejectNullName() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Customer(
                        "CUST002",
                        null,
                        "shams@gmail.com",
                        "123456"
                )
        );
    }

    @Test
    void constructorShouldRejectBlankName() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Customer(
                        "CUST002",
                        "   ",
                        "shams@gmail.com",
                        "123456"
                )
        );
    }

    @Test
    void constructorShouldRejectNullEmail() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Customer(
                        "CUST002",
                        "Shams",
                        null,
                        "123456"
                )
        );
    }

    @Test
    void constructorShouldRejectBlankEmail() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Customer(
                        "CUST002",
                        "Shams",
                        "   ",
                        "123456"
                )
        );
    }

    @Test
    void constructorShouldRejectNullPassword() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Customer(
                        "CUST002",
                        "Shams",
                        "shams@gmail.com",
                        null
                )
        );
    }

    @Test
    void constructorShouldRejectBlankPassword() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Customer(
                        "CUST002",
                        "Shams",
                        "shams@gmail.com",
                        "   "
                )
        );
    }

    // ===========Password Tests============
    @Test
    void changePasswordShouldWorkSuccessfully() {

        assertDoesNotThrow(
                () -> customer.changePassword("newPassword123")
        );
    }

    @Test
    void changePasswordShouldRejectNullPassword() {

        assertThrows(
                IllegalArgumentException.class,
                () -> customer.changePassword(null)
        );
    }

    @Test
    void changePasswordShouldRejectBlankPassword() {

        assertThrows(
                IllegalArgumentException.class,
                () -> customer.changePassword("   ")
        );
    }

    // ============Encapsulation Tests================

    @Test
    void customerDataShouldRemainUnchangedAfterPasswordChange() {

        customer.changePassword("newPassword123");

        assertEquals("CUST001", customer.getCustomerId());
        assertEquals("Shams", customer.getName());
        assertEquals("shams@gmail.com", customer.getEmail());
    }
}