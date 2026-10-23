package com.learning.banking.service;

import com.learning.banking.model.Customer;
import com.learning.banking.model.StandardAccount;
import com.learning.banking.repository.inmemory.InMemoryAccountRepository;
import com.learning.banking.repository.inmemory.InMemoryCustomerRepository;
import com.learning.banking.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceCloseAccountTest {

    private InMemoryAccountRepository accountRepository;
    private AccountService accountService;
    private Customer owner;

    @BeforeEach
    void setUp() {
        accountRepository = new InMemoryAccountRepository();
        InMemoryCustomerRepository customerRepository =
                new InMemoryCustomerRepository();

        owner = new Customer("CUST001", "Shams", "shams@gmail.com", "123456");
        customerRepository.save(owner);

        accountService =
                new AccountServiceImpl(accountRepository, customerRepository);
    }

    @Test
    void shouldCloseAnAccountWithZeroBalance() {

        accountService.openAccount(new StandardAccount("ACC001", 0, owner));

        accountService.closeAccount("ACC001");

        assertThrows(
                IllegalArgumentException.class,
                () -> accountService.findAccountById("ACC001")
        );
        assertTrue(accountRepository.existsByIdIncludingInactive("ACC001"));
    }

    @Test
    void shouldRefuseToCloseAnAccountThatStillHoldsMoney() {

        accountService.openAccount(new StandardAccount("ACC001", 500, owner));

        assertThrows(
                IllegalStateException.class,
                () -> accountService.closeAccount("ACC001")
        );

        assertNotNull(accountService.findAccountById("ACC001"));
    }

    @Test
    void shouldCloseAfterTheBalanceIsWithdrawn() {

        accountService.openAccount(new StandardAccount("ACC001", 500, owner));
        accountService.withdraw("ACC001", 500);

        assertDoesNotThrow(() -> accountService.closeAccount("ACC001"));
    }

    @Test
    void shouldFailToCloseAnUnknownAccount() {

        assertThrows(
                IllegalArgumentException.class,
                () -> accountService.closeAccount("NOPE")
        );
    }

    @Test
    void closedAccountShouldNotAcceptDepositsOrWithdrawals() {

        accountService.openAccount(new StandardAccount("ACC001", 0, owner));
        accountService.closeAccount("ACC001");

        assertThrows(IllegalArgumentException.class,
                () -> accountService.deposit("ACC001", 10));
        assertThrows(IllegalArgumentException.class,
                () -> accountService.withdraw("ACC001", 10));
    }

    @Test
    void closedAccountIdShouldNeverBeReused() {

        accountService.openAccount(new StandardAccount("ACC001", 0, owner));
        accountService.closeAccount("ACC001");

        assertThrows(
                IllegalArgumentException.class,
                () -> accountService.openAccount(
                        new StandardAccount("ACC001", 100, owner))
        );
    }
}
