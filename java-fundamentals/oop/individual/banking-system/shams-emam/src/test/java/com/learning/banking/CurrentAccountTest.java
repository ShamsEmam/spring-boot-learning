package com.learning.banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrentAccountTest {

    private Customer customer;
    private CurrentAccount currentAccount;

    private static final double DELTA = 0.001;

    @BeforeEach
    void setUp() {

        customer = new Customer(
                "CUST001",
                "Shams",
                "shams@gmail.com",
                "123456"
        );

        currentAccount = new CurrentAccount(
                "CUR001",
                5_000.0,
                customer,
                2_000.0
        );
    }

    // ===============Constructor Tests=================

    @Test
    void constructorShouldCreateCurrentAccountSuccessfully() {

        assertEquals("CUR001", currentAccount.getAccountId());

        assertEquals(
                5_000.0,
                currentAccount.getBalance(),
                DELTA
        );

        assertEquals(
                2_000.0,
                currentAccount.getOverdraftLimit(),
                DELTA
        );

        assertEquals(
                customer,
                currentAccount.getOwner()
        );
    }

    @Test
    void constructorShouldRejectNegativeOverdraftLimit() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new CurrentAccount(
                        "CUR002",
                        5_000.0,
                        customer,
                        -1_000.0
                )
        );
    }

    @Test
    void constructorShouldAllowZeroOverdraftLimit() {

        CurrentAccount account =
                new CurrentAccount(
                        "CUR002",
                        5_000.0,
                        customer,
                        0.0
                );

        assertEquals(
                0.0,
                account.getOverdraftLimit(),
                DELTA
        );
    }

    // =============Normal Withdrawal================

    @Test
    void withdrawShouldDecreaseBalanceNormally() {

        double withdrawn =
                currentAccount.withdraw(2_000.0);

        assertEquals(
                2_000.0,
                withdrawn,
                DELTA
        );

        assertEquals(
                3_000.0,
                currentAccount.getBalance(),
                DELTA
        );
    }

    // =============Overdraft Tests===============
    @Test
    void withdrawShouldAllowUsingOverdraft() {

        currentAccount.withdraw(6_000.0);

        assertEquals(
                -1_000.0,
                currentAccount.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawShouldAllowBalanceToReachExactOverdraftLimit() {

        currentAccount.withdraw(7_000.0);

        assertEquals(
                -2_000.0,
                currentAccount.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawShouldRejectWhenOverdraftLimitWouldBeExceeded() {

        assertThrows(
                IllegalStateException.class,
                () -> currentAccount.withdraw(7_001.0)
        );

        assertEquals(
                5_000.0,
                currentAccount.getBalance(),
                DELTA
        );
    }

    // =============Invalid Amount Tests===============

    @Test
    void withdrawWithZeroAmountShouldThrowException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> currentAccount.withdraw(0.0)
        );

        assertEquals(
                5_000.0,
                currentAccount.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawWithNegativeAmountShouldThrowException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> currentAccount.withdraw(-500.0)
        );

        assertEquals(
                5_000.0,
                currentAccount.getBalance(),
                DELTA
        );
    }

    // =============Inherited Deposit Behavior================

    @Test
    void depositShouldIncreaseBalance() {

        currentAccount.deposit(1_500.0);

        assertEquals(
                6_500.0,
                currentAccount.getBalance(),
                DELTA
        );
    }

    @Test
    void depositShouldReduceNegativeBalance() {

        currentAccount.withdraw(6_000.0);

        assertEquals(
                -1_000.0,
                currentAccount.getBalance(),
                DELTA
        );

        currentAccount.deposit(500.0);

        assertEquals(
                -500.0,
                currentAccount.getBalance(),
                DELTA
        );
    }

    // ==========Transfer + Overdraft=============


    @Test
    void transferShouldAllowUsingOverdraft() {

        Account destination = new Account(
                "ACC002",
                1_000.0,
                customer
        );

        currentAccount.transfer(
                destination,
                6_000.0
        );

        assertEquals(
                -1_000.0,
                currentAccount.getBalance(),
                DELTA
        );

        assertEquals(
                7_000.0,
                destination.getBalance(),
                DELTA
        );
    }

    @Test
    void transferShouldRejectWhenOverdraftLimitWouldBeExceeded() {

        Account destination = new Account(
                "ACC002",
                1_000.0,
                customer
        );

        assertThrows(
                IllegalStateException.class,
                () -> currentAccount.transfer(
                        destination,
                        8_000.0
                )
        );

        assertEquals(
                5_000.0,
                currentAccount.getBalance(),
                DELTA
        );

        assertEquals(
                1_000.0,
                destination.getBalance(),
                DELTA
        );
    }

    // ==========Polymorphism===============
    @Test
    void accountReferenceShouldUseCurrentAccountWithdrawalRule() {

        Account account = currentAccount;

        account.withdraw(6_000.0);

        assertEquals(
                -1_000.0,
                account.getBalance(),
                DELTA
        );
    }
}