package com.learning.banking.service;

import com.learning.banking.model.Account;
import com.learning.banking.model.Customer;
import com.learning.banking.model.SavingsAccount;
import com.learning.banking.model.StandardAccount;
import com.learning.banking.repository.AccountRepository;
import com.learning.banking.repository.CustomerRepository;
import com.learning.banking.repository.inmemory.InMemoryAccountRepository;
import com.learning.banking.repository.inmemory.InMemoryCustomerRepository;
import com.learning.banking.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    private static final double DELTA = 0.001;

    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;
    private AccountService accountService;

    private Customer customer;

    @BeforeEach
    void setUp() {

        accountRepository =
                new InMemoryAccountRepository();

        customerRepository =
                new InMemoryCustomerRepository();

        accountService =
                new AccountServiceImpl(
                        accountRepository,
                        customerRepository
                );

        customer = new Customer(
                "CUST001",
                "Shams",
                "shams@gmail.com",
                "123456"
        );

        customerRepository.save(customer);
    }

    @Test
    void openAccountShouldOpenAccountSuccessfully() {

        Account account =
                new StandardAccount(
                        "ACC001",
                        1000.0,
                        customer
                );

        accountService.openAccount(account);

        Account foundAccount =
                accountService.findAccountById("ACC001");

        assertNotNull(foundAccount);
        assertEquals(account, foundAccount);
    }

    @Test
    void openAccountShouldRejectNullAccount() {

        assertThrows(
                IllegalArgumentException.class,
                () -> accountService.openAccount(null)
        );
    }

    @Test
    void openAccountShouldRejectUnregisteredCustomer() {

        Customer unregisteredCustomer =
                new Customer(
                        "CUST999",
                        "Ahmed",
                        "ahmed@gmail.com",
                        "123456"
                );

        Account account =
                new StandardAccount(
                        "ACC001",
                        1000.0,
                        unregisteredCustomer
                );

        assertThrows(
                IllegalStateException.class,
                () -> accountService.openAccount(account)
        );
    }

    @Test
    void openAccountShouldRejectDuplicateAccountId() {

        Account firstAccount =
                new StandardAccount(
                        "ACC001",
                        1000.0,
                        customer
                );

        Account duplicateAccount =
                new SavingsAccount(
                        "ACC001",
                        2000.0,
                        customer,
                        5.0,
                        500.0
                );

        accountService.openAccount(firstAccount);

        assertThrows(
                IllegalArgumentException.class,
                () -> accountService.openAccount(
                        duplicateAccount
                )
        );
    }

    @Test
    void depositShouldIncreaseAccountBalance() {

        Account account =
                new StandardAccount(
                        "ACC001",
                        1000.0,
                        customer
                );

        accountService.openAccount(account);

        accountService.deposit(
                "ACC001",
                500.0
        );

        assertEquals(
                1500.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawShouldDecreaseAccountBalance() {

        Account account =
                new StandardAccount(
                        "ACC001",
                        1000.0,
                        customer
                );

        accountService.openAccount(account);

        double withdrawn =
                accountService.withdraw(
                        "ACC001",
                        300.0
                );

        assertEquals(
                300.0,
                withdrawn,
                DELTA
        );

        assertEquals(
                700.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void withdrawShouldRespectAccountPolicy() {

        Account account =
                new SavingsAccount(
                        "SAV001",
                        1000.0,
                        customer,
                        5.0,
                        500.0
                );

        accountService.openAccount(account);

        assertThrows(
                IllegalStateException.class,
                () -> accountService.withdraw(
                        "SAV001",
                        600.0
                )
        );

        assertEquals(
                1000.0,
                account.getBalance(),
                DELTA
        );
    }

    @Test
    void findAccountByIdShouldReturnExistingAccount() {

        Account account =
                new StandardAccount(
                        "ACC001",
                        1000.0,
                        customer
                );

        accountService.openAccount(account);

        Account foundAccount =
                accountService.findAccountById("ACC001");

        assertEquals(account, foundAccount);
    }

    @Test
    void findAccountByIdShouldRejectMissingAccount() {

        assertThrows(
                IllegalArgumentException.class,
                () -> accountService.findAccountById(
                        "ACC999"
                )
        );
    }

    @Test
    void depositShouldRejectMissingAccount() {

        assertThrows(
                IllegalArgumentException.class,
                () -> accountService.deposit(
                        "ACC999",
                        500.0
                )
        );
    }

    @Test
    void withdrawShouldRejectMissingAccount() {

        assertThrows(
                IllegalArgumentException.class,
                () -> accountService.withdraw(
                        "ACC999",
                        500.0
                )
        );
    }

    @Test
    void constructorShouldRejectNullAccountRepository() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new AccountServiceImpl(
                        null,
                        customerRepository
                )
        );
    }

    @Test
    void constructorShouldRejectNullCustomerRepository() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new AccountServiceImpl(
                        accountRepository,
                        null
                )
        );
    }
}