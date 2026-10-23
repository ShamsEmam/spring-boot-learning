package com.learning.banking.model;

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
    public AccountType getType() {
        return AccountType.CURRENT;
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

    /**
     * Rebuilds a current account that was loaded from the database.
     *
     * A normal constructor refuses a negative balance (you cannot OPEN an
     * account already in debt), but a saved current account can legitimately
     * be overdrawn. We start at zero and replay the balance through
     * deposit()/withdraw(), so the overdraft rule is still checked: a stored
     * balance beyond the limit is reported instead of silently accepted.
     */
    public static CurrentAccount restore(
            String accountId,
            double balance,
            Customer owner,
            double overdraftLimit) {

        CurrentAccount account =
                new CurrentAccount(accountId, 0.0, owner, overdraftLimit);

        if (balance > 0) {
            account.deposit(balance);
        } else if (balance < 0) {
            account.withdraw(-balance);
        }

        return account;
    }
}
