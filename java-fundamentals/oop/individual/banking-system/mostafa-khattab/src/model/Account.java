package model;

import java.util.Objects;

public abstract class Account implements Withdrawable {
    private final String accountNumber;
    private double balance;
    private final Customer accountHolder;

    public Account(String accountNumber, double balance, Customer accountHolder) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountHolder = accountHolder;
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

    @Override
    public abstract boolean canWithdraw(double amount);

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