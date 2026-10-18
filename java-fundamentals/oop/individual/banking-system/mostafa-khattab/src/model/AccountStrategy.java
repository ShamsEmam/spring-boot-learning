
package model;

public interface AccountStrategy {

    boolean canWithdraw(double balance, double amount, double overdraftLimit);

    double calculateInterest(double balance, double interestRate);
}
