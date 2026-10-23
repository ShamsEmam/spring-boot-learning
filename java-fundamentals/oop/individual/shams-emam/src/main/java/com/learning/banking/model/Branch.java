package com.learning.banking.model;

import com.learning.banking.util.Validator;

public class Branch {

    private final String branchId;
    private final String name;
    private final String address;
    private final String phone;
    private final String email;

    private String manager;

    public Branch(
            String branchId,
            String name,
            String address,
            String phone,
            String email,
            String manager) {

        Validator.requireNotBlank(branchId, "Branch ID");
        Validator.requireNotBlank(name, "Branch name");
        Validator.requireNotBlank(address, "Branch address");
        Validator.requireNotBlank(phone, "Branch phone");
        Validator.requireNotBlank(email, "Branch email");
        Validator.requireNotBlank(manager, "Branch manager");

        if (branchId.matches(".*\\s.*")) {
            throw new IllegalArgumentException(
                    "Branch ID cannot contain spaces"
            );
        }

        this.branchId = branchId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.manager = manager;
    }

    public String getBranchId() {
        return branchId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getManager() {
        return manager;
    }

    public void changeManager(String newManager) {

        Validator.requireNotBlank(
                newManager,
                "Branch manager"
        );

        manager = newManager;
    }

}