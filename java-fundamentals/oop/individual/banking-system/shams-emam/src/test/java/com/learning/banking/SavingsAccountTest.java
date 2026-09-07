package com.learning.banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SavingsAccountTest {

    private static final double DELTA = 0.001;

    private Customer customer;
    private SavingsAccount account;

    @BeforeEach
    void setUp() {

        customer = new Customer(
                "CUST001",
                "Shams",
                "shams@gmail.com",
                "123456"
        );

        account = new SavingsAccount(
                "SAV001",
                10_000.0,
                customer,
                5.0,
                500.0
        );
    }

    @Test
    void addInterestShouldIncreaseBalance() {

        account.addInterest();

        assertEquals(
                10_500.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawShouldPreserveMinimumBalance() {

        account.withdraw(9_500.0);

        assertEquals(
                500.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawShouldRejectBelowMinimumBalance() {

        assertThrows(
                IllegalStateException.class,
                () -> account.withdraw(9_501.0)
        );

        assertEquals(
                10_000.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void accountReferenceShouldUseSavingsPolicy() {

        Account parentReference = account;

        assertThrows(
                IllegalStateException.class,
                () -> parentReference.withdraw(9_501.0)
        );
    }
}