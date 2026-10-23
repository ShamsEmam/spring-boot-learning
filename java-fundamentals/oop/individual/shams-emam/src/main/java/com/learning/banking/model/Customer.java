package com.learning.banking.model;

import com.learning.banking.util.Validator;

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

        Validator.requireNotBlank(customerId, "Customer ID");
        Validator.requireNotBlank(name, "Customer name");
        Validator.requireNotBlank(email, "Customer email");
        Validator.requireNotBlank(password, "Password");

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

        Validator.requireNotBlank(
                newPassword,
                "Password"
        );

        this.password = newPassword;
    }
    public String getPassword() {
        return password;
    }
}