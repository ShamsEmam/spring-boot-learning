package com.learning.banking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountTypeTest {

    private final Customer owner =
            new Customer("CUST001", "Shams", "shams@gmail.com", "123456");

    @Test
    void everyAccountShouldReportItsOwnType() {

        assertEquals(AccountType.STANDARD,
                new StandardAccount("A1", 100, owner).getType());

        assertEquals(AccountType.SAVINGS,
                new SavingsAccount("A2", 1000, owner, 5, 100).getType());

        assertEquals(AccountType.CURRENT,
                new CurrentAccount("A3", 100, owner, 500).getType());
    }

    @Test
    void limitedAccountShouldNotReportItselfAsStandard() {

        // LimitedAccount extends StandardAccount, but it is its own type.
        assertEquals(AccountType.LIMITED,
                new LimitedAccount("A4", 100, owner, 50).getType());
    }
}
