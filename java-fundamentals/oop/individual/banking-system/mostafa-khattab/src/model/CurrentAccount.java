package model;

public class CurrentAccount extends Account {
    private double overdraftLimit;

    public CurrentAccount(String accountNumber, double balance, Customer accountHolder, double overdraftLimit) {
        super(accountNumber, balance, accountHolder);
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    public boolean canWithdraw(double amount) {
        return amount > 0 && (getBalance() + overdraftLimit >= amount);
    }

    @Override
    public String toString() {
        return String.format("CurrentAccount[Number=%s, Balance=%.2f, OverdraftLimit=%.2f, Holder=%s]",
                getAccountNumber(), getBalance(), overdraftLimit, getAccountHolder().getName());
    }
}