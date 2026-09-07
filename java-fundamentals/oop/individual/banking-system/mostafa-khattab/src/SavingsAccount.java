public class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 0.10;

    public SavingsAccount(String accountNumber, double balance, Customer accountHolder) {
        super(accountNumber, balance, accountHolder);
    }

    public double calculateInterest() {
        return getBalance() * INTEREST_RATE;
    }

    public void applyInterest() {
        double interest = calculateInterest();
        if (interest > 0) {
            deposit(interest);
        }
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > getBalance()) {
            return false;
        }
        updateBalance(getBalance() - amount);
        return true;
    }

    @Override
    public String toString() {
        return String.format("SavingsAccount[Number=%s, Balance=%.2f, InterestRate=%.1f%%, Holder=%s]",
                getAccountNumber(), getBalance(), INTEREST_RATE * 100, getAccountHolder().getName());
    }
}