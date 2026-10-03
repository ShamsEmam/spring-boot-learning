package com.learning.banking.service;

import com.learning.banking.model.Branch;
import com.learning.banking.repository.BranchRepository;
import com.learning.banking.repository.inmemory.InMemoryBranchRepository;
import com.learning.banking.service.impl.BranchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BranchServiceTest {

    private BranchRepository branchRepository;
    private BranchService branchService;
    private Branch branch;

    @BeforeEach
    void setUp() {

        branchRepository =
                new InMemoryBranchRepository();

        branchService =
                new BranchServiceImpl(branchRepository);

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
    void addBranchShouldAddBranchSuccessfully() {

        branchService.addBranch(branch);

        Branch foundBranch =
                branchService.findBranchById("BR001");

        assertNotNull(foundBranch);
        assertEquals(branch, foundBranch);
    }

    @Test
    void addBranchShouldRejectNullBranch() {

        assertThrows(
                IllegalArgumentException.class,
                () -> branchService.addBranch(null)
        );
    }

    @Test
    void addBranchShouldRejectDuplicateBranchId() {

        Branch duplicateBranch =
                new Branch(
                        "BR001",
                        "Nasr City Branch",
                        "Nasr City, Cairo",
                        "01011111111",
                        "nasrcity@shamsbank.com",
                        "Mohamed Hassan"
                );

        branchService.addBranch(branch);

        assertThrows(
                IllegalArgumentException.class,
                () -> branchService.addBranch(
                        duplicateBranch
                )
        );
    }

    @Test
    void findBranchByIdShouldReturnExistingBranch() {

        branchService.addBranch(branch);

        Branch foundBranch =
                branchService.findBranchById("BR001");

        assertEquals(branch, foundBranch);
    }

    @Test
    void findBranchByIdShouldRejectMissingBranch() {

        assertThrows(
                IllegalArgumentException.class,
                () -> branchService.findBranchById(
                        "BR999"
                )
        );
    }

    @Test
    void getAllBranchesShouldReturnAllBranches() {

        Branch secondBranch =
                new Branch(
                        "BR002",
                        "Nasr City Branch",
                        "Nasr City, Cairo",
                        "01011111111",
                        "nasrcity@shamsbank.com",
                        "Mohamed Hassan"
                );

        branchService.addBranch(branch);
        branchService.addBranch(secondBranch);

        List<Branch> branches =
                branchService.getAllBranches();

        assertEquals(2, branches.size());
        assertTrue(branches.contains(branch));
        assertTrue(branches.contains(secondBranch));
    }

    @Test
    void getAllBranchesShouldReturnEmptyListWhenNoBranchesExist() {

        List<Branch> branches =
                branchService.getAllBranches();

        assertNotNull(branches);
        assertTrue(branches.isEmpty());
    }

    @Test
    void changeBranchManagerShouldChangeManagerSuccessfully() {

        branchService.addBranch(branch);

        branchService.changeBranchManager(
                "BR001",
                "Mohamed Hassan"
        );

        Branch updatedBranch =
                branchService.findBranchById("BR001");

        assertEquals(
                "Mohamed Hassan",
                updatedBranch.getManager()
        );
    }

    @Test
    void changeBranchManagerShouldRejectMissingBranch() {

        assertThrows(
                IllegalArgumentException.class,
                () -> branchService.changeBranchManager(
                        "BR999",
                        "Mohamed Hassan"
                )
        );
    }

    @Test
    void changeBranchManagerShouldRejectNullManager() {

        branchService.addBranch(branch);

        assertThrows(
                IllegalArgumentException.class,
                () -> branchService.changeBranchManager(
                        "BR001",
                        null
                )
        );

        assertEquals(
                "Ahmed Ali",
                branch.getManager()
        );
    }

    @Test
    void changeBranchManagerShouldRejectBlankManager() {

        branchService.addBranch(branch);

        assertThrows(
                IllegalArgumentException.class,
                () -> branchService.changeBranchManager(
                        "BR001",
                        "   "
                )
        );

        assertEquals(
                "Ahmed Ali",
                branch.getManager()
        );
    }

    @Test
    void constructorShouldRejectNullRepository() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new BranchServiceImpl(null)
        );
    }
}