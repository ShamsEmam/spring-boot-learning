
package model;

public class SavingAccountStrategy implements AccountStrategy {

    @Override
    public boolean canWithdraw(double balance, double amount, double overdraftLimit) {
        return amount > 0 && amount <= balance;
    }

    @Override
    public double calculateInterest(double balance, double interestRate) {
        return balance * interestRate;
    }
}
