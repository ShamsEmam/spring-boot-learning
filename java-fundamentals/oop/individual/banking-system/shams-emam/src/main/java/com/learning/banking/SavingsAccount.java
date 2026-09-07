package com.learning.banking;

public class SavingsAccount extends Account {

    private final double interestRate;
    private final double minimumBalance;

    public SavingsAccount(
            String accountId,
            double initialBalance,
            Customer owner,
            double interestRate,
            double minimumBalance) {

        super(accountId, initialBalance, owner);

        if (!Double.isFinite(interestRate)
                || interestRate < 0
                || interestRate > 100) {

            throw new IllegalArgumentException(
                    "Interest rate must be between 0 and 100"
            );
        }

        if (!Double.isFinite(minimumBalance)
                || minimumBalance < 0) {

            throw new IllegalArgumentException(
                    "Minimum balance must be a valid non-negative amount"
            );
        }

        if (minimumBalance > initialBalance) {
            throw new IllegalArgumentException(
                    "Minimum balance cannot exceed the initial balance"
            );
        }

        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double getMinimumBalance() {
        return minimumBalance;
    }

    public void addInterest() {

        double interestAmount =
                getBalance() * interestRate / 100.0;

        if (interestAmount > 0) {
            deposit(interestAmount);
        }
    }

    @Override
    protected void validateWithdrawalPolicy(double amount) {

        double remainingBalance =
                getBalance() - amount;

        if (remainingBalance < minimumBalance) {
            throw new IllegalStateException(
                    "Withdrawal would reduce the balance below the minimum balance"
            );
        }
    }
}