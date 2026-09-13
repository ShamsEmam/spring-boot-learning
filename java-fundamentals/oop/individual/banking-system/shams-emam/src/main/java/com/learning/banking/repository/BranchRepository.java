package com.learning.banking.repository;

import com.learning.banking.model.Branch;

import java.util.List;

public interface BranchRepository {

    void save(Branch branch);

    Branch findById(String branchId);

    boolean existsById(String branchId);

    List<Branch> findAll();
}