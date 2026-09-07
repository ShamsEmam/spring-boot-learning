package com.learning.banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrentAccountTest {

    private static final double DELTA = 0.001;

    private Customer customer;
    private CurrentAccount account;

    @BeforeEach
    void setUp() {

        customer = new Customer(
                "CUST001",
                "Shams",
                "shams@gmail.com",
                "123456"
        );

        account = new CurrentAccount(
                "CUR001",
                5_000.0,
                customer,
                2_000.0
        );
    }

    @Test
    void withdrawShouldAllowUsingOverdraft() {

        account.withdraw(6_000.0);

        assertEquals(
                -1_000.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawShouldAllowExactOverdraftLimit() {

        account.withdraw(7_000.0);

        assertEquals(
                -2_000.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawShouldRejectBeyondOverdraftLimit() {

        assertThrows(
                IllegalStateException.class,
                () -> account.withdraw(7_001.0)
        );

        assertEquals(
                5_000.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void accountReferenceShouldUseCurrentAccountPolicy() {

        Account parentReference = account;

        parentReference.withdraw(6_000.0);

        assertEquals(
                -1_000.0,
                parentReference.getBalance(),
                DELTA
        );
    }
}