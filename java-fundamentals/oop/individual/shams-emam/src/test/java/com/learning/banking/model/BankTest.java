package com.learning.banking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {

    @Test
    void constructorShouldCreateBankSuccessfully() {

        Bank bank = new Bank(
                "Shams Bank",
                "Maadi, Cairo",
                "01012345678"
        );

        assertEquals(
                "Shams Bank",
                bank.getBankName()
        );

        assertEquals(
                "Maadi, Cairo",
                bank.getBankAddress()
        );

        assertEquals(
                "01012345678",
                bank.getBankPhone()
        );
    }

    @Test
    void constructorShouldRejectNullBankName() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Bank(
                        null,
                        "Maadi, Cairo",
                        "01012345678"
                )
        );
    }

    @Test
    void constructorShouldRejectBlankBankName() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Bank(
                        "   ",
                        "Maadi, Cairo",
                        "01012345678"
                )
        );
    }

    @Test
    void constructorShouldRejectNullAddress() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Bank(
                        "Shams Bank",
                        null,
                        "01012345678"
                )
        );
    }

    @Test
    void constructorShouldRejectBlankAddress() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Bank(
                        "Shams Bank",
                        "   ",
                        "01012345678"
                )
        );
    }

    @Test
    void constructorShouldRejectNullPhone() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Bank(
                        "Shams Bank",
                        "Maadi, Cairo",
                        null
                )
        );
    }

    @Test
    void constructorShouldRejectBlankPhone() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Bank(
                        "Shams Bank",
                        "Maadi, Cairo",
                        "   "
                )
        );
    }
}