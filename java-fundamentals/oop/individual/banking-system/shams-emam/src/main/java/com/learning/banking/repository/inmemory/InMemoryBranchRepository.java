package com.learning.banking.repository.inmemory;

import com.learning.banking.model.Branch;
import com.learning.banking.repository.BranchRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryBranchRepository
        implements BranchRepository {

    private final List<Branch> branches =
            new ArrayList<>();

    @Override
    public void save(Branch branch) {

        if (branch == null) {
            throw new IllegalArgumentException(
                    "Branch cannot be null"
            );
        }

        if (existsById(branch.getBranchId())) {
            throw new IllegalArgumentException(
                    "Branch ID already exists"
            );
        }

        branches.add(branch);
    }

    @Override
    public Branch findById(String branchId) {

        validateId(branchId);

        for (Branch branch : branches) {
            if (branch.getBranchId().equals(branchId)) {
                return branch;
            }
        }

        return null;
    }

    @Override
    public boolean existsById(String branchId) {
        return findById(branchId) != null;
    }

    @Override
    public List<Branch> findAll() {
        return List.copyOf(branches);
    }

    private void validateId(String branchId) {

        if (branchId == null || branchId.isBlank()) {
            throw new IllegalArgumentException(
                    "Branch ID cannot be null or blank"
            );
        }
    }
}