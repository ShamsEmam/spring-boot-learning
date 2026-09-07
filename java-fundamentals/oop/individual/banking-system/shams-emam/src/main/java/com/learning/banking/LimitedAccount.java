package com.learning.banking;

public class LimitedAccount extends StandardAccount {

    private final double withdrawalLimit;

    public LimitedAccount(
            String accountId,
            double initialBalance,
            Customer owner,
            double withdrawalLimit) {

        super(accountId, initialBalance, owner);

        if (!Double.isFinite(withdrawalLimit)
                || withdrawalLimit <= 0) {

            throw new IllegalArgumentException(
                    "Withdrawal limit must be a valid number greater than zero"
            );
        }

        this.withdrawalLimit = withdrawalLimit;
    }

    public double getWithdrawalLimit() {
        return withdrawalLimit;
    }

    @Override
    protected void validateWithdrawalPolicy(double amount) {

        super.validateWithdrawalPolicy(amount);

        if (amount > withdrawalLimit) {
            throw new IllegalStateException(
                    "Withdrawal amount exceeds the allowed withdrawal limit"
            );
        }
    }
}