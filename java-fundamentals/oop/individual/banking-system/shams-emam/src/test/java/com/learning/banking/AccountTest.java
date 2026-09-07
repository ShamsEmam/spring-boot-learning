package com.learning.banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private static final double DELTA = 0.001;

    private Customer customer;
    private Account account;

    @BeforeEach
    void setUp() {

        customer = new Customer(
                "CUST001",
                "Shams",
                "shams@gmail.com",
                "123456"
        );

        account = new StandardAccount(
                "ACC001",
                1000.0,
                customer
        );
    }

    @Test
    void constructorShouldCreateAccountSuccessfully() {

        assertEquals("ACC001", account.getAccountId());
        assertEquals(1000.0, account.getBalance(), DELTA);
        assertEquals(customer, account.getOwner());
    }

    @Test
    void constructorShouldRejectNullAccountId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new StandardAccount(
                        null,
                        1000.0,
                        customer
                )
        );
    }

    @Test
    void constructorShouldRejectBlankAccountId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new StandardAccount(
                        "   ",
                        1000.0,
                        customer
                )
        );
    }

    @Test
    void constructorShouldRejectNegativeBalance() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new StandardAccount(
                        "ACC002",
                        -100.0,
                        customer
                )
        );
    }

    @Test
    void constructorShouldRejectNullOwner() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new StandardAccount(
                        "ACC002",
                        1000.0,
                        null
                )
        );
    }

    @Test
    void depositShouldIncreaseBalance() {

        account.deposit(500.0);

        assertEquals(
                1500.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void invalidDepositShouldNotChangeBalance() {

        assertThrows(
                IllegalArgumentException.class,
                () -> account.deposit(-100.0)
        );

        assertEquals(
                1000.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawShouldDecreaseBalance() {

        account.withdraw(300.0);

        assertEquals(
                700.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawShouldRejectInsufficientBalance() {

        assertThrows(
                IllegalStateException.class,
                () -> account.withdraw(1500.0)
        );

        assertEquals(
                1000.0,
                account.getBalance(),
                DELTA
        );
    }
}