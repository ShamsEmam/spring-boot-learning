package com.learning.banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Customer customer;
    private Account account;

    private static final double DELTA = 0.001;

    @BeforeEach
    void setUp() {

        customer = new Customer(
                "CUST001",
                "Shams",
                "shams@gmail.com",
                "123456"
        );

        account = new Account(
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
                () -> new Account(null, 1000.0, customer)
        );
    }

    @Test
    void constructorShouldRejectBlankAccountId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Account("   ", 1000.0, customer)
        );
    }

    @Test
    void constructorShouldRejectAccountIdContainingSpaces() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Account("ACC 001", 1000.0, customer)
        );
    }

    @Test
    void constructorShouldRejectNegativeInitialBalance() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Account("ACC002", -100.0, customer)
        );
    }

    @Test
    void constructorShouldRejectNullOwner() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Account("ACC002", 1000.0, null)
        );
    }

    @Test
    void depositShouldIncreaseBalance() {

        // Act
        account.deposit(500.0);

        // Assert
        assertEquals(
                1500.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void depositWithZeroAmountShouldThrowException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> account.deposit(0)
        );

        assertEquals(
                1000.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void depositWithNegativeAmountShouldThrowException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> account.deposit(-100)
        );

        assertEquals(
                1000.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawShouldDecreaseBalance() {

        double withdrawnAmount = account.withdraw(300.0);

        assertEquals(
                300.0,
                withdrawnAmount,
                DELTA
        );

        assertEquals(
                700.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawWithZeroAmountShouldThrowException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> account.withdraw(0)
        );

        assertEquals(
                1000.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawWithNegativeAmountShouldThrowException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> account.withdraw(-100)
        );

        assertEquals(
                1000.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawMoreThanBalanceShouldThrowException() {

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


    @Test
    void transferShouldMoveMoneyBetweenAccounts() {

        Account destination = new Account(
                "ACC002",
                500.0,
                customer
        );

        account.transfer(destination, 300.0);

        assertEquals(
                700.0,
                account.getBalance(),
                DELTA
        );

        assertEquals(
                800.0,
                destination.getBalance(),
                DELTA
        );
    }

    @Test
    void transferWithNegativeAmountShouldThrowException() {

        Account destination = new Account(
                "ACC002",
                500.0,
                customer
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> account.transfer(destination, -100.0)
        );

        assertEquals(
                1000.0,
                account.getBalance(),
                DELTA
        );

        assertEquals(
                500.0,
                destination.getBalance(),
                DELTA
        );
    }

    @Test
    void transferToNullAccountShouldThrowException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> account.transfer(null, 100.0)
        );
    }

    @Test
    void transferToSameAccountShouldThrowException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> account.transfer(account, 100.0)
        );

        assertEquals(
                1000.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void transferMoreThanBalanceShouldThrowException() {

        Account destination = new Account(
                "ACC002",
                500.0,
                customer
        );

        assertThrows(
                IllegalStateException.class,
                () -> account.transfer(destination, 1500.0)
        );

        assertEquals(
                1000.0,
                account.getBalance(),
                DELTA
        );

        assertEquals(
                500.0,
                destination.getBalance(),
                DELTA
        );
    }
}