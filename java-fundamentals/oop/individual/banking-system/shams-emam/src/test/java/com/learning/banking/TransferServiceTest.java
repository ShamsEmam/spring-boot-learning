package com.learning.banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransferServiceTest {

    private static final double DELTA = 0.001;

    private Customer customer;
    private TransferService transferService;

    @BeforeEach
    void setUp() {

        customer = new Customer(
                "CUST001",
                "Shams",
                "shams@gmail.com",
                "123456"
        );

        transferService =
                new BankTransferService();
    }

    @Test
    void transferShouldMoveMoneyBetweenAccounts() {

        Account source =
                new StandardAccount(
                        "ACC001",
                        1000.0,
                        customer
                );

        Account destination =
                new StandardAccount(
                        "ACC002",
                        500.0,
                        customer
                );

        transferService.transfer(
                source,
                destination,
                300.0
        );

        assertEquals(
                700.0,
                source.getBalance(),
                DELTA
        );

        assertEquals(
                800.0,
                destination.getBalance(),
                DELTA
        );
    }

    @Test
    void transferShouldRespectSavingsPolicy() {

        Account source =
                new SavingsAccount(
                        "SAV001",
                        1000.0,
                        customer,
                        5.0,
                        500.0
                );

        Account destination =
                new StandardAccount(
                        "ACC002",
                        500.0,
                        customer
                );

        assertThrows(
                IllegalStateException.class,
                () -> transferService.transfer(
                        source,
                        destination,
                        600.0
                )
        );

        assertEquals(
                1000.0,
                source.getBalance(),
                DELTA
        );
    }

    @Test
    void transferShouldRespectCurrentAccountPolicy() {

        Account source =
                new CurrentAccount(
                        "CUR001",
                        500.0,
                        customer,
                        1000.0
                );

        Account destination =
                new StandardAccount(
                        "ACC002",
                        500.0,
                        customer
                );

        transferService.transfer(
                source,
                destination,
                1200.0
        );

        assertEquals(
                -700.0,
                source.getBalance(),
                DELTA
        );

        assertEquals(
                1700.0,
                destination.getBalance(),
                DELTA
        );
    }

    @Test
    void transferShouldRespectLimitedAccountPolicy() {

        Account source =
                new LimitedAccount(
                        "LIM001",
                        5000.0,
                        customer,
                        1000.0
                );

        Account destination =
                new StandardAccount(
                        "ACC002",
                        500.0,
                        customer
                );

        assertThrows(
                IllegalStateException.class,
                () -> transferService.transfer(
                        source,
                        destination,
                        1500.0
                )
        );
    }

    @Test
    void transferShouldRejectSameAccount() {

        Account account =
                new StandardAccount(
                        "ACC001",
                        1000.0,
                        customer
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> transferService.transfer(
                        account,
                        account,
                        100.0
                )
        );
    }

    @Test
    void transferShouldRejectNullSource() {

        Account destination =
                new StandardAccount(
                        "ACC002",
                        500.0,
                        customer
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> transferService.transfer(
                        null,
                        destination,
                        100.0
                )
        );
    }

    @Test
    void transferShouldRejectNullDestination() {

        Account source =
                new StandardAccount(
                        "ACC001",
                        1000.0,
                        customer
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> transferService.transfer(
                        source,
                        null,
                        100.0
                )
        );
    }
}