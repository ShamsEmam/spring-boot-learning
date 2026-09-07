package com.learning.banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LimitedAccountTest {

    private static final double DELTA = 0.001;

    private Customer customer;
    private LimitedAccount account;

    @BeforeEach
    void setUp() {

        customer = new Customer(
                "CUST001",
                "Shams",
                "shams@gmail.com",
                "123456"
        );

        account = new LimitedAccount(
                "LIM001",
                5_000.0,
                customer,
                1_000.0
        );
    }

    @Test
    void withdrawShouldAllowAmountBelowLimit() {

        account.withdraw(500.0);

        assertEquals(
                4_500.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawShouldAllowExactLimit() {

        account.withdraw(1_000.0);

        assertEquals(
                4_000.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawShouldRejectAmountAboveLimit() {

        assertThrows(
                IllegalStateException.class,
                () -> account.withdraw(1_001.0)
        );

        assertEquals(
                5_000.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawShouldStillRespectAvailableBalance() {

        LimitedAccount smallAccount =
                new LimitedAccount(
                        "LIM002",
                        500.0,
                        customer,
                        1_000.0
                );

        assertThrows(
                IllegalStateException.class,
                () -> smallAccount.withdraw(700.0)
        );
    }
}