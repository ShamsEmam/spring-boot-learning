package com.learning.banking;

public class CurrentAccount extends Account {
    private final double overdraftLimit;

    public CurrentAccount(String accountId, double initialBalance, Customer owner, double overdraftLimit) {
        super(accountId, initialBalance, owner);
        if (overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit must be at least zero");
        }
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    public double withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Withdrawal amount must be greater than zero"
            );
        }
        if (getBalance() - amount < -overdraftLimit) {
            throw new IllegalStateException(
                    "Withdrawal would exceed the overdraft limit"
            );
        }
        deductBalance(amount);
        return amount;
    }

}
