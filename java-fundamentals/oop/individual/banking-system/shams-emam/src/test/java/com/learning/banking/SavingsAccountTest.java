package com.learning.banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SavingsAccountTest {

    private Customer customer;
    private SavingsAccount savingsAccount;

    private static final double DELTA = 0.001;

    @BeforeEach
    void setUp() {

        customer = new Customer(
                "CUST001",
                "Shams",
                "shams@gmail.com",
                "123456"
        );

        savingsAccount = new SavingsAccount(
                "SAV001",
                10_000.0,
                customer,
                5.0,
                500.0
        );
    }


    @Test
    void constructorShouldCreateSavingsAccountSuccessfully() {

        assertEquals("SAV001", savingsAccount.getAccountId());

        assertEquals(
                10_000.0,
                savingsAccount.getBalance(),
                DELTA
        );

        assertEquals(
                5.0,
                savingsAccount.getInterestRate(),
                DELTA
        );

        assertEquals(
                500.0,
                savingsAccount.getMinimumBalance(),
                DELTA
        );

        assertEquals(customer, savingsAccount.getOwner());
    }

    @Test
    void constructorShouldRejectNegativeInterestRate() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new SavingsAccount(
                        "SAV002",
                        10_000.0,
                        customer,
                        -5.0,
                        500.0
                )
        );
    }

    @Test
    void constructorShouldRejectInterestRateGreaterThan100() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new SavingsAccount(
                        "SAV002",
                        10_000.0,
                        customer,
                        101.0,
                        500.0
                )
        );
    }

    @Test
    void constructorShouldRejectNegativeMinimumBalance() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new SavingsAccount(
                        "SAV002",
                        10_000.0,
                        customer,
                        5.0,
                        -500.0
                )
        );
    }

    @Test
    void constructorShouldRejectMinimumBalanceGreaterThanInitialBalance() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new SavingsAccount(
                        "SAV002",
                        1_000.0,
                        customer,
                        5.0,
                        2_000.0
                )
        );
    }

    @Test
    void addInterestShouldIncreaseBalance() {

        // Act
        savingsAccount.addInterest();

        // Assert
        assertEquals(
                10_500.0,
                savingsAccount.getBalance(),
                DELTA
        );
    }

    @Test
    void zeroInterestRateShouldNotChangeBalance() {

        SavingsAccount zeroInterestAccount =
                new SavingsAccount(
                        "SAV002",
                        10_000.0,
                        customer,
                        0.0,
                        500.0
                );

        zeroInterestAccount.addInterest();

        assertEquals(
                10_000.0,
                zeroInterestAccount.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawShouldDecreaseBalanceWhenMinimumBalanceIsPreserved() {

        double withdrawnAmount =
                savingsAccount.withdraw(2_000.0);

        assertEquals(
                2_000.0,
                withdrawnAmount,
                DELTA
        );

        assertEquals(
                8_000.0,
                savingsAccount.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawShouldAllowBalanceToReachExactlyMinimumBalance() {

        savingsAccount.withdraw(9_500.0);

        assertEquals(
                500.0,
                savingsAccount.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawShouldRejectWhenBalanceWouldFallBelowMinimumBalance() {

        assertThrows(
                IllegalStateException.class,
                () -> savingsAccount.withdraw(9_600.0)
        );

        // Failed operation must not change the balance
        assertEquals(
                10_000.0,
                savingsAccount.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawWithZeroAmountShouldThrowException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> savingsAccount.withdraw(0.0)
        );

        assertEquals(
                10_000.0,
                savingsAccount.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawWithNegativeAmountShouldThrowException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> savingsAccount.withdraw(-500.0)
        );

        assertEquals(
                10_000.0,
                savingsAccount.getBalance(),
                DELTA
        );
    }

    @Test
    void depositShouldUseInheritedAccountBehavior() {

        savingsAccount.deposit(2_000.0);

        assertEquals(
                12_000.0,
                savingsAccount.getBalance(),
                DELTA
        );
    }

    @Test
    void accountReferenceShouldUseSavingsWithdrawalRule() {

        Account account = savingsAccount;

        assertThrows(
                IllegalStateException.class,
                () -> account.withdraw(9_600.0)
        );

        assertEquals(
                10_000.0,
                account.getBalance(),
                DELTA
        );
    }
}