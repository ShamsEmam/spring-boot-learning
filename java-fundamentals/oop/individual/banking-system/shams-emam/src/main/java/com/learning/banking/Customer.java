package com.learning.banking;
public class Customer {

    private final String customerId;
    private final String name;
    private final String email;
    private String password;

    public Customer(String customerId, String name, String email, String password) {
        if (customerId == null || name == null || email == null || password == null) {
            throw new IllegalArgumentException("Customer information cannot be null");
        }

        if (customerId.isBlank() || name.isBlank() || email.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("Customer information cannot be blank");
        }

        if (customerId.matches(".*\\s.*")) {
            throw new IllegalArgumentException("Customer ID cannot contain spaces");
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
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }

        this.password = newPassword;
    }
}