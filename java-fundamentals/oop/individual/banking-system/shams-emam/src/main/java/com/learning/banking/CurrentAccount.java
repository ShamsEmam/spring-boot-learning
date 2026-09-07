package com.learning.banking;

public class CurrentAccount extends Account {

    private final double overdraftLimit;

    public CurrentAccount(
            String accountId,
            double initialBalance,
            Customer owner,
            double overdraftLimit) {

        super(accountId, initialBalance, owner);

        if (!Double.isFinite(overdraftLimit)
                || overdraftLimit < 0) {

            throw new IllegalArgumentException(
                    "Overdraft limit must be a valid non-negative amount"
            );
        }

        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    protected void validateWithdrawalPolicy(double amount) {

        double remainingBalance =
                getBalance() - amount;

        if (remainingBalance < -overdraftLimit) {
            throw new IllegalStateException(
                    "Withdrawal would exceed the overdraft limit"
            );
        }
    }
}