package model;

import java.util.Objects;

public class Account implements Withdrawable {

    public enum AccountType {
        SAVING,
        CURRENT
    }

    private final String accountNumber;
    private double balance;
    private final Customer accountHolder;
    private final AccountType accountType;
    private double overdraftLimit;
    private double interestRate;
    private AccountStrategy strategy;

    public Account(
            String accountNumber,
            double balance,
            Customer accountHolder,
            AccountType accountType,
            double overdraftLimit,
            double interestRate,
            AccountStrategy strategy) {

        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountHolder = accountHolder;
        this.accountType = accountType;
        this.overdraftLimit = overdraftLimit;
        this.interestRate = interestRate;
        this.strategy = Objects.requireNonNull(strategy);
    }

    @Override
    public boolean canWithdraw(double amount) {
        return strategy.canWithdraw(balance, amount, overdraftLimit);
    }

    public double calcInterest() {
        return strategy.calculateInterest(balance, interestRate);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Customer getAccountHolder() {
        return accountHolder;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public double getInterestRate() {
        return interestRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}