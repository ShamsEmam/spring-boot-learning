public class CurrentAccount extends Account {

    private final double overdraftLimit;


    public CurrentAccount(String accountNumber, double balance, Customer accountHolder, double overdraftLimit) {
        super(accountNumber, balance, accountHolder);
        this.overdraftLimit = overdraftLimit;
    }

    private boolean isOverdraftExceeded(double amount) {
        return (getBalance() - amount) < -overdraftLimit;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0 || isOverdraftExceeded(amount)) {
            return false;
        }

        updateBalance(getBalance() - amount);
        return true;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    public String toString() {
        return String.format("CurrentAccount[Number=%s, Balance=%.2f, OverdraftLimit=%.2f, Holder=%s]",
                getAccountNumber(), getBalance(), overdraftLimit, getAccountHolder().getName());
    }
}