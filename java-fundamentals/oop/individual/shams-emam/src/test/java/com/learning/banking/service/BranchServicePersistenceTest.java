package com.learning.banking.service;

import com.learning.banking.model.Branch;
import com.learning.banking.repository.inmemory.InMemoryBranchRepository;
import com.learning.banking.service.impl.BranchServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Regression test for a real bug: changeBranchManager changed the object in
 * memory but never saved it, so a database-backed repository kept the OLD
 * manager. The in-memory repository hid the bug because it stores the very
 * same object. This spy records every save() call to prove the save happens.
 */
class BranchServicePersistenceTest {

    private static class RecordingBranchRepository extends InMemoryBranchRepository {

        final List<String> savedManagers = new ArrayList<>();

        @Override
        public void save(Branch branch) {
            savedManagers.add(branch.getManager());
            super.save(branch);
        }
    }

    @Test
    void changeBranchManagerShouldSaveTheBranch() {

        RecordingBranchRepository repository = new RecordingBranchRepository();
        BranchService service = new BranchServiceImpl(repository);

        service.addBranch(new Branch(
                "BR001", "Maadi", "Maadi, Cairo", "0100", "m@bank.com", "Old Manager"));

        service.changeBranchManager("BR001", "New Manager");

        assertEquals(List.of("Old Manager", "New Manager"), repository.savedManagers);
    }
}
