package com.learning.banking;
//package main.java.com.learning.banking;

public class Account implements Transferable {

    private final String accountId;
    private final Customer owner;
    private double balance;

    public Account(String accountId, double initialBalance, Customer owner) {

        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException(
                    "Account ID cannot be null or blank"
            );
        }

        if (accountId.matches(".*\\s.*")) {
            throw new IllegalArgumentException(
                    "Account ID cannot contain spaces"
            );
        }

        if (!Double.isFinite(initialBalance) || initialBalance < 0) {
            throw new IllegalArgumentException(
                    "Initial balance must be a valid non-negative amount"
            );
        }

        if (owner == null) {
            throw new IllegalArgumentException(
                    "Account must have an owner"
            );
        }

        this.accountId = accountId;
        this.balance = initialBalance;
        this.owner = owner;
    }


    public String getAccountId() {
        return accountId;
    }

    public Customer getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }



    public void deposit(double amount) {
        validatePositiveAmount(amount, "Deposit");

        balance += amount;
    }


    public double withdraw(double amount) {
        validatePositiveAmount(amount, "Withdrawal");

        if (amount > balance) {
            throw new IllegalStateException(
                    "Insufficient balance"
            );
        }

        deductBalance(amount);

        return amount;
    }


    @Override
    public void transfer(Account destination, double amount) {

        if (destination == null) {
            throw new IllegalArgumentException(
                    "Destination account cannot be null"
            );
        }

        if (destination == this) {
            throw new IllegalArgumentException(
                    "Cannot transfer to the same account"
            );
        }

        validatePositiveAmount(amount, "Transfer");

        withdraw(amount);
        destination.deposit(amount);
    }



    protected void deductBalance(double amount) {
        balance -= amount;
    }

    protected void validateWithdrawalAmount(double amount) {
        validatePositiveAmount(amount, "Withdrawal");
    }



    private void validatePositiveAmount(double amount, String operation) {

        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException(
                    operation + " amount must be a valid number greater than zero"
            );
        }
    }
}