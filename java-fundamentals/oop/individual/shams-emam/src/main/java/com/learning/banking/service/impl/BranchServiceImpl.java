package com.learning.banking.service.impl;

import com.learning.banking.model.Branch;
import com.learning.banking.repository.BranchRepository;
import com.learning.banking.service.BranchService;

import java.util.List;

public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;

    public BranchServiceImpl(
            BranchRepository branchRepository) {

        if (branchRepository == null) {
            throw new IllegalArgumentException(
                    "Branch repository cannot be null"
            );
        }

        this.branchRepository = branchRepository;
    }

    @Override
    public void addBranch(Branch branch) {

        if (branch == null) {
            throw new IllegalArgumentException(
                    "Branch cannot be null"
            );
        }

        if (branchRepository.existsById(branch.getBranchId())) {
            throw new IllegalArgumentException(
                    "Branch ID already exists"
            );
        }

        branchRepository.save(branch);
    }

    @Override
    public Branch findBranchById(String branchId) {
        return getExistingBranch(branchId);
    }

    @Override
    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    @Override
    public void changeBranchManager(
            String branchId,
            String newManager) {

        Branch branch =
                getExistingBranch(branchId);

        branch.changeManager(newManager);

        branchRepository.save(branch);
    }

    private Branch getExistingBranch(String branchId) {

        Branch branch =
                branchRepository.findById(branchId);

        if (branch == null) {
            throw new IllegalArgumentException(
                    "Branch not found: " + branchId
            );
        }

        return branch;
    }
}