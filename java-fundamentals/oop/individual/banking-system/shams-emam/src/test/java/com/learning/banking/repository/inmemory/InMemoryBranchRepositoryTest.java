package com.learning.banking.repository.inmemory;

import com.learning.banking.model.Branch;
import com.learning.banking.repository.BranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryBranchRepositoryTest {

    private BranchRepository branchRepository;
    private Branch branch;

    @BeforeEach
    void setUp() {

        branchRepository =
                new InMemoryBranchRepository();

        branch = new Branch(
                "BR001",
                "Maadi Branch",
                "Maadi, Cairo",
                "01098765432",
                "maadi@shamsbank.com",
                "Ahmed Ali"
        );
    }

    @Test
    void saveShouldSaveBranchSuccessfully() {

        branchRepository.save(branch);

        Branch foundBranch =
                branchRepository.findById("BR001");

        assertEquals(branch, foundBranch);
    }

    @Test
    void saveShouldRejectNullBranch() {

        assertThrows(
                IllegalArgumentException.class,
                () -> branchRepository.save(null)
        );
    }

    @Test
    void saveShouldRejectDuplicateBranchId() {

        Branch duplicateBranch =
                new Branch(
                        "BR001",
                        "Nasr City Branch",
                        "Nasr City, Cairo",
                        "01011111111",
                        "nasrcity@shamsbank.com",
                        "Mohamed Hassan"
                );

        branchRepository.save(branch);

        assertThrows(
                IllegalArgumentException.class,
                () -> branchRepository.save(
                        duplicateBranch
                )
        );
    }

    @Test
    void findByIdShouldReturnExistingBranch() {

        branchRepository.save(branch);

        Branch foundBranch =
                branchRepository.findById("BR001");

        assertNotNull(foundBranch);
        assertEquals(branch, foundBranch);
    }

    @Test
    void findByIdShouldReturnNullWhenBranchDoesNotExist() {

        Branch foundBranch =
                branchRepository.findById("BR999");

        assertNull(foundBranch);
    }

    @Test
    void findByIdShouldRejectNullId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> branchRepository.findById(null)
        );
    }

    @Test
    void findByIdShouldRejectBlankId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> branchRepository.findById("   ")
        );
    }

    @Test
    void existsByIdShouldReturnTrueWhenBranchExists() {

        branchRepository.save(branch);

        assertTrue(
                branchRepository.existsById("BR001")
        );
    }

    @Test
    void existsByIdShouldReturnFalseWhenBranchDoesNotExist() {

        assertFalse(
                branchRepository.existsById("BR999")
        );
    }

    @Test
    void findAllShouldReturnAllBranches() {

        Branch secondBranch =
                new Branch(
                        "BR002",
                        "Nasr City Branch",
                        "Nasr City, Cairo",
                        "01011111111",
                        "nasrcity@shamsbank.com",
                        "Mohamed Hassan"
                );

        branchRepository.save(branch);
        branchRepository.save(secondBranch);

        List<Branch> branches =
                branchRepository.findAll();

        assertEquals(2, branches.size());
        assertTrue(branches.contains(branch));
        assertTrue(branches.contains(secondBranch));
    }

    @Test
    void findAllShouldReturnEmptyListWhenNoBranchesExist() {

        List<Branch> branches =
                branchRepository.findAll();

        assertNotNull(branches);
        assertTrue(branches.isEmpty());
    }

    @Test
    void findAllShouldReturnReadOnlyList() {

        branchRepository.save(branch);

        List<Branch> branches =
                branchRepository.findAll();

        assertThrows(
                UnsupportedOperationException.class,
                () -> branches.clear()
        );

        assertEquals(
                1,
                branchRepository.findAll().size()
        );
    }
}