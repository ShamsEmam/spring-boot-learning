package com.learning.banking.service;

import com.learning.banking.model.Account;
import com.learning.banking.model.Customer;
import com.learning.banking.model.StandardAccount;
import com.learning.banking.repository.inmemory.InMemoryAccountRepository;
import com.learning.banking.service.impl.TransferServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A transfer must reach storage through ONE saveAll() call (one database
 * transaction), never as two separate save() calls.
 */
class TransferAtomicityTest {

    private static class CountingRepository extends InMemoryAccountRepository {

        int saveCalls;
        int saveAllCalls;
        int accountsInLastSaveAll;

        @Override
        public void save(Account account) {
            saveCalls++;
            super.save(account);
        }

        @Override
        public void saveAll(List<Account> accounts) {
            saveAllCalls++;
            accountsInLastSaveAll = accounts.size();
            super.saveAll(accounts);
        }
    }

    @Test
    void transferShouldStoreBothAccountsInOneSaveAllCall() {

        Customer owner = new Customer("CUST001", "Shams", "shams@gmail.com", "123456");
        CountingRepository repository = new CountingRepository();
        repository.save(new StandardAccount("SRC", 1_000, owner));
        repository.save(new StandardAccount("DST", 0, owner));
        repository.saveCalls = 0;

        new TransferServiceImpl(repository).transfer("SRC", "DST", 400);

        assertEquals(1, repository.saveAllCalls);
        assertEquals(2, repository.accountsInLastSaveAll);
        assertEquals(0, repository.saveCalls);
        assertEquals(600, repository.findById("SRC").getBalance());
        assertEquals(400, repository.findById("DST").getBalance());
    }
}
