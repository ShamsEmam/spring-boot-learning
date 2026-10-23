package com.learning.banking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrentAccountRestoreTest {

    private final Customer owner =
            new Customer("CUST001", "Shams", "shams@gmail.com", "123456");

    @Test
    void shouldRestoreAnOverdrawnAccount() {

        CurrentAccount account =
                CurrentAccount.restore("CUR001", -1_000.0, owner, 2_000.0);

        assertEquals(-1_000.0, account.getBalance());
        assertEquals(2_000.0, account.getOverdraftLimit());
    }

    @Test
    void shouldRestorePositiveAndZeroBalances() {

        assertEquals(750.5,
                CurrentAccount.restore("C1", 750.5, owner, 100).getBalance());

        assertEquals(0.0,
                CurrentAccount.restore("C2", 0.0, owner, 100).getBalance());
    }

    @Test
    void shouldReportStoredBalanceBeyondTheOverdraftLimit() {

        assertThrows(
                IllegalStateException.class,
                () -> CurrentAccount.restore("CUR001", -2_500.0, owner, 2_000.0)
        );
    }

    @Test
    void restoredAccountShouldStillEnforceTheOverdraftLimit() {

        CurrentAccount account =
                CurrentAccount.restore("CUR001", -1_000.0, owner, 2_000.0);

        assertThrows(IllegalStateException.class, () -> account.withdraw(1_500.0));
    }

    @Test
    void openingAnAccountWithNegativeBalanceShouldStillBeRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new CurrentAccount("CUR001", -1.0, owner, 2_000.0)
        );
    }
}
