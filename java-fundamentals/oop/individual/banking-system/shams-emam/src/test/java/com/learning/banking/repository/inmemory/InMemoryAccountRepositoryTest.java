package com.learning.banking.repository.inmemory;

import com.learning.banking.model.Account;
import com.learning.banking.model.Customer;
import com.learning.banking.model.StandardAccount;
import com.learning.banking.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryAccountRepositoryTest {

    private AccountRepository accountRepository;
    private Customer customer;

    @BeforeEach
    void setUp() {

        accountRepository =
                new InMemoryAccountRepository();

        customer = new Customer(
                "CUST001",
                "Shams",
                "shams@gmail.com",
                "123456"
        );
    }

    @Test
    void saveShouldSaveAccountSuccessfully() {

        Account account =
                new StandardAccount(
                        "ACC001",
                        1000.0,
                        customer
                );

        accountRepository.save(account);

        Account foundAccount =
                accountRepository.findById("ACC001");

        assertEquals(account, foundAccount);
    }

    @Test
    void saveShouldRejectNullAccount() {

        assertThrows(
                IllegalArgumentException.class,
                () -> accountRepository.save(null)
        );
    }

    @Test
    void saveShouldRejectDuplicateAccountId() {

        Account firstAccount =
                new StandardAccount(
                        "ACC001",
                        1000.0,
                        customer
                );

        Account duplicateAccount =
                new StandardAccount(
                        "ACC001",
                        2000.0,
                        customer
                );

        accountRepository.save(firstAccount);

        assertThrows(
                IllegalArgumentException.class,
                () -> accountRepository.save(
                        duplicateAccount
                )
        );
    }

    @Test
    void findByIdShouldReturnExistingAccount() {

        Account account =
                new StandardAccount(
                        "ACC001",
                        1000.0,
                        customer
                );

        accountRepository.save(account);

        Account foundAccount =
                accountRepository.findById("ACC001");

        assertNotNull(foundAccount);
        assertEquals(account, foundAccount);
    }

    @Test
    void findByIdShouldReturnNullWhenAccountDoesNotExist() {

        Account foundAccount =
                accountRepository.findById("ACC999");

        assertNull(foundAccount);
    }

    @Test
    void findByIdShouldRejectNullId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> accountRepository.findById(null)
        );
    }

    @Test
    void findByIdShouldRejectBlankId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> accountRepository.findById("   ")
        );
    }

    @Test
    void existsByIdShouldReturnTrueWhenAccountExists() {

        Account account =
                new StandardAccount(
                        "ACC001",
                        1000.0,
                        customer
                );

        accountRepository.save(account);

        assertTrue(
                accountRepository.existsById("ACC001")
        );
    }

    @Test
    void existsByIdShouldReturnFalseWhenAccountDoesNotExist() {

        assertFalse(
                accountRepository.existsById("ACC999")
        );
    }

    @Test
    void findAllShouldReturnAllAccounts() {

        Account firstAccount =
                new StandardAccount(
                        "ACC001",
                        1000.0,
                        customer
                );

        Account secondAccount =
                new StandardAccount(
                        "ACC002",
                        2000.0,
                        customer
                );

        accountRepository.save(firstAccount);
        accountRepository.save(secondAccount);

        List<Account> accounts =
                accountRepository.findAll();

        assertEquals(2, accounts.size());
        assertTrue(accounts.contains(firstAccount));
        assertTrue(accounts.contains(secondAccount));
    }

    @Test
    void findAllShouldReturnEmptyListWhenNoAccountsExist() {

        List<Account> accounts =
                accountRepository.findAll();

        assertNotNull(accounts);
        assertTrue(accounts.isEmpty());
    }

    @Test
    void findAllShouldReturnReadOnlyList() {

        Account account =
                new StandardAccount(
                        "ACC001",
                        1000.0,
                        customer
                );

        accountRepository.save(account);

        List<Account> accounts =
                accountRepository.findAll();

        assertThrows(
                UnsupportedOperationException.class,
                () -> accounts.clear()
        );

        assertEquals(
                1,
                accountRepository.findAll().size()
        );
    }
}