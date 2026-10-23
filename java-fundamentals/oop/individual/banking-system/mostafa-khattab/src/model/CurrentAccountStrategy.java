
package model;

public class CurrentAccountStrategy implements AccountStrategy {

    @Override
    public boolean canWithdraw(double balance, double amount, double overdraftLimit) {
        return amount > 0 && (balance - amount) >= -overdraftLimit;
    }

    @Override
    public double calculateInterest(double balance, double interestRate) {
        return 0.0;
    }
}
