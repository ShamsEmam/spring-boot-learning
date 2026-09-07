package com.learning.banking;

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

        validateRequiredField(branchId, "Branch ID");
        validateRequiredField(name, "Branch name");
        validateRequiredField(address, "Branch address");
        validateRequiredField(phone, "Branch phone");
        validateRequiredField(email, "Branch email");
        validateRequiredField(manager, "Branch manager");

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

        validateRequiredField(
                newManager,
                "Branch manager"
        );

        manager = newManager;
    }

    private static void validateRequiredField(
            String value,
            String fieldName) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be null or blank"
            );
        }
    }
}