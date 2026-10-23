package com.learning.banking.repository.inmemory;

import com.learning.banking.model.Account;
import com.learning.banking.model.Customer;
import com.learning.banking.model.StandardAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryAccountRepositorySoftDeleteTest {

    private InMemoryAccountRepository repository;
    private Customer owner;
    private Account account;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
        owner = new Customer("CUST001", "Shams", "shams@gmail.com", "123456");
        account = new StandardAccount("ACC001", 500, owner);
        repository.save(account);
    }

    @Test
    void deactivatedAccountShouldBeInvisibleToFindAndExists() {

        repository.deactivateById("ACC001");

        assertNull(repository.findById("ACC001"));
        assertFalse(repository.existsById("ACC001"));
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void deactivatedAccountShouldStillCountAsAUsedId() {

        repository.deactivateById("ACC001");

        assertTrue(repository.existsByIdIncludingInactive("ACC001"));
        assertFalse(repository.existsByIdIncludingInactive("OTHER"));
    }

    @Test
    void deactivatingAnUnknownAccountShouldFail() {

        assertThrows(
                IllegalArgumentException.class,
                () -> repository.deactivateById("NOPE")
        );
    }

    @Test
    void deactivatingTwiceShouldFail() {

        repository.deactivateById("ACC001");

        assertThrows(
                IllegalArgumentException.class,
                () -> repository.deactivateById("ACC001")
        );
    }

    @Test
    void saveShouldRefuseToOverwriteAClosedAccount() {

        repository.deactivateById("ACC001");

        Account replacement = new StandardAccount("ACC001", 999, owner);

        assertThrows(
                IllegalStateException.class,
                () -> repository.save(replacement)
        );
    }

    @Test
    void otherAccountsShouldStayVisible() {

        repository.save(new StandardAccount("ACC002", 100, owner));

        repository.deactivateById("ACC001");

        assertEquals(1, repository.findAll().size());
        assertNotNull(repository.findById("ACC002"));
    }

    @Test
    void saveAllShouldSaveEveryAccount() {

        Account second = new StandardAccount("ACC002", 100, owner);
        Account third = new StandardAccount("ACC003", 200, owner);

        repository.saveAll(List.of(second, third));

        assertEquals(3, repository.findAll().size());
    }

    @Test
    void saveAllShouldSaveNothingWhenOneItemIsInvalid() {

        Account valid = new StandardAccount("ACC002", 100, owner);

        assertThrows(
                IllegalArgumentException.class,
                () -> repository.saveAll(Arrays.asList(valid, null))
        );

        assertFalse(repository.existsById("ACC002"));
    }

    @Test
    void saveAllShouldRejectNullList() {

        assertThrows(
                IllegalArgumentException.class,
                () -> repository.saveAll(null)
        );
    }
}
