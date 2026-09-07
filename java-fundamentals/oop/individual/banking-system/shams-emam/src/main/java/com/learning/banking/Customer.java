package com.learning.banking;

public class Customer {

    private final String customerId;
    private final String name;
    private final String email;

    private String password;

    public Customer(
            String customerId,
            String name,
            String email,
            String password) {

        validateRequiredField(customerId, "Customer ID");
        validateRequiredField(name, "Customer name");
        validateRequiredField(email, "Customer email");
        validateRequiredField(password, "Password");

        if (customerId.matches(".*\\s.*")) {
            throw new IllegalArgumentException(
                    "Customer ID cannot contain spaces"
            );
        }

        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void changePassword(String newPassword) {

        validateRequiredField(
                newPassword,
                "Password"
        );

        this.password = newPassword;
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