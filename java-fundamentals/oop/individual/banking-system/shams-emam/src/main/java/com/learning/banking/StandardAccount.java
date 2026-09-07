package com.learning.banking;

public class StandardAccount extends Account {

    public StandardAccount(
            String accountId,
            double initialBalance,
            Customer owner) {

        super(accountId, initialBalance, owner);
    }

    @Override
    protected void validateWithdrawalPolicy(double amount) {

        if (amount > getBalance()) {
            throw new IllegalStateException(
                    "Insufficient balance"
            );
        }
    }
}