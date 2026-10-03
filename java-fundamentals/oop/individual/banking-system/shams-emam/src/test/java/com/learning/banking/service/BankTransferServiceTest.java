package com.learning.banking.service;

import com.learning.banking.model.Account;
import com.learning.banking.model.CurrentAccount;
import com.learning.banking.model.Customer;
import com.learning.banking.model.LimitedAccount;
import com.learning.banking.model.SavingsAccount;
import com.learning.banking.model.StandardAccount;
import com.learning.banking.repository.AccountRepository;
import com.learning.banking.repository.inmemory.InMemoryAccountRepository;
import com.learning.banking.service.impl.BankTransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankTransferServiceTest {

    private static final double DELTA = 0.001;

    private AccountRepository accountRepository;
    private TransferService transferService;
    private Customer customer;

    @BeforeEach
    void setUp() {

        accountRepository =
                new InMemoryAccountRepository();

        transferService =
                new BankTransferService(accountRepository);

        customer = new Customer(
                "CUST001",
                "Shams",
                "shams@gmail.com",
                "123456"
        );
    }

    @Test
    void transferShouldMoveMoneySuccessfully() {

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

        accountRepository.save(source);
        accountRepository.save(destination);

        transferService.transfer(
                "ACC001",
                "ACC002",
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
    void transferShouldRespectSavingsAccountPolicy() {

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

        accountRepository.save(source);
        accountRepository.save(destination);

        assertThrows(
                IllegalStateException.class,
                () -> transferService.transfer(
                        "SAV001",
                        "ACC002",
                        600.0
                )
        );

        assertEquals(
                1000.0,
                source.getBalance(),
                DELTA
        );

        assertEquals(
                500.0,
                destination.getBalance(),
                DELTA
        );
    }

    @Test
    void transferShouldRespectCurrentAccountOverdraft() {

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

        accountRepository.save(source);
        accountRepository.save(destination);

        transferService.transfer(
                "CUR001",
                "ACC002",
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
    void transferShouldRejectWhenOverdraftLimitExceeded() {

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

        accountRepository.save(source);
        accountRepository.save(destination);

        assertThrows(
                IllegalStateException.class,
                () -> transferService.transfer(
                        "CUR001",
                        "ACC002",
                        1600.0
                )
        );

        assertEquals(
                500.0,
                source.getBalance(),
                DELTA
        );

        assertEquals(
                500.0,
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

        accountRepository.save(source);
        accountRepository.save(destination);

        assertThrows(
                IllegalStateException.class,
                () -> transferService.transfer(
                        "LIM001",
                        "ACC002",
                        1500.0
                )
        );

        assertEquals(
                5000.0,
                source.getBalance(),
                DELTA
        );

        assertEquals(
                500.0,
                destination.getBalance(),
                DELTA
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

        accountRepository.save(account);

        assertThrows(
                IllegalArgumentException.class,
                () -> transferService.transfer(
                        "ACC001",
                        "ACC001",
                        100.0
                )
        );
    }

    @Test
    void transferShouldRejectInvalidAmount() {

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

        accountRepository.save(source);
        accountRepository.save(destination);

        assertThrows(
                IllegalArgumentException.class,
                () -> transferService.transfer(
                        "ACC001",
                        "ACC002",
                        -100.0
                )
        );

        assertEquals(
                1000.0,
                source.getBalance(),
                DELTA
        );

        assertEquals(
                500.0,
                destination.getBalance(),
                DELTA
        );
    }

    @Test
    void transferShouldRejectZeroAmount() {

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

        accountRepository.save(source);
        accountRepository.save(destination);

        assertThrows(
                IllegalArgumentException.class,
                () -> transferService.transfer(
                        "ACC001",
                        "ACC002",
                        0.0
                )
        );
    }

    @Test
    void transferShouldRejectMissingSourceAccount() {

        Account destination =
                new StandardAccount(
                        "ACC002",
                        500.0,
                        customer
                );

        accountRepository.save(destination);

        assertThrows(
                IllegalArgumentException.class,
                () -> transferService.transfer(
                        "ACC999",
                        "ACC002",
                        100.0
                )
        );

        assertEquals(
                500.0,
                destination.getBalance(),
                DELTA
        );
    }

    @Test
    void transferShouldRejectMissingDestinationAccount() {

        Account source =
                new StandardAccount(
                        "ACC001",
                        1000.0,
                        customer
                );

        accountRepository.save(source);

        assertThrows(
                IllegalArgumentException.class,
                () -> transferService.transfer(
                        "ACC001",
                        "ACC999",
                        100.0
                )
        );

        assertEquals(
                1000.0,
                source.getBalance(),
                DELTA
        );
    }

    @Test
    void transferShouldRejectNullSourceAccountId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> transferService.transfer(
                        null,
                        "ACC002",
                        100.0
                )
        );
    }

    @Test
    void transferShouldRejectNullDestinationAccountId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> transferService.transfer(
                        "ACC001",
                        null,
                        100.0
                )
        );
    }

    @Test
    void constructorShouldRejectNullRepository() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new BankTransferService(null)
        );
    }
}