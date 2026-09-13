package com.learning.banking.service;

import com.learning.banking.model.Branch;

import java.util.List;

public interface BranchService {

    void addBranch(Branch branch);

    Branch findBranchById(String branchId);

    List<Branch> getAllBranches();

    void changeBranchManager(
            String branchId,
            String newManager
    );
}