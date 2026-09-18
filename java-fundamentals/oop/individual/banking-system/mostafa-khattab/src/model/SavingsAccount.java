package model;

public class SavingsAccount extends Account implements Interestable {
    private double interestRate;

    public SavingsAccount(String accountNumber, double balance, Customer accountHolder, double interestRate) {
        super(accountNumber, balance, accountHolder);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    @Override
    public boolean canWithdraw(double amount) {
        return amount > 0 && getBalance() >= amount;
    }

    @Override
    public double calcInterest() {
        return getBalance() * interestRate;
    }

    @Override
    public String toString() {
        return String.format("SavingsAccount[Number=%s, Balance=%.2f, InterestRate=%.1f%%, Holder=%s]",
                getAccountNumber(), getBalance(), interestRate * 100, getAccountHolder().getName());
    }
}