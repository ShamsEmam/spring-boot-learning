package com.learning.banking.repository.inmemory;

import com.learning.banking.model.Branch;
import com.learning.banking.repository.BranchRepository;
import com.learning.banking.util.Validator;

import java.util.ArrayList;
import java.util.List;

public class InMemoryBranchRepository implements BranchRepository {

    private final List<Branch> branches = new ArrayList<>();

    @Override
    public void save(Branch branch) {
        if (branch == null) {
            throw new IllegalArgumentException("Branch cannot be null");
        }

        for (int i = 0; i < branches.size(); i++) {
            if (branches.get(i).getBranchId().equals(branch.getBranchId())) {
                branches.set(i, branch);
                return;
            }
        }

        branches.add(branch);
    }

    @Override
    public Branch findById(String branchId) {
        Validator.requireNotBlank(branchId, "Branch ID");

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
}
