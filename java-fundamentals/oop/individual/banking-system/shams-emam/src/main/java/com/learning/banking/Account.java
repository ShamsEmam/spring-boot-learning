package com.learning.banking;

public abstract class Account
        implements Withdrawable, Depositable {

    private final String accountId;
    private final Customer owner;
    private double balance;

    public Account(
            String accountId,
            double initialBalance,
            Customer owner) {

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

        if (!Double.isFinite(initialBalance)
                || initialBalance < 0) {

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

    @Override
    public void deposit(double amount) {

        validatePositiveAmount(amount, "Deposit");

        balance += amount;
    }

    @Override
    public final double withdraw(double amount) {

        validatePositiveAmount(amount, "Withdrawal");

        validateWithdrawalPolicy(amount);

        balance -= amount;

        return amount;
    }

    protected abstract void validateWithdrawalPolicy(
            double amount
    );

    private void validatePositiveAmount(
            double amount,
            String operation) {

        if (!Double.isFinite(amount)
                || amount <= 0) {

            throw new IllegalArgumentException(
                    operation
                            + " amount must be a valid number greater than zero"
            );
        }
    }
}