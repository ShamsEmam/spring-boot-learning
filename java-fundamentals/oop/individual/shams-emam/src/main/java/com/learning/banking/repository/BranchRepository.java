package com.learning.banking.repository;

import com.learning.banking.model.Branch;

import java.util.List;

/**
 * Storage for branchs.
 *
 * save() inserts OR updates. Rules such as "no duplicate IDs" belong to the
 * service layer so that every implementation behaves identically.
 */
public interface BranchRepository {

    void save(Branch branch);

    Branch findById(String branchId);

    boolean existsById(String branchId);

    List<Branch> findAll();
}