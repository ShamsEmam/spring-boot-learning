public class CurrentAccount extends Account {

    private final double overdraftLimit;

    public CurrentAccount(String accountNumber, double balance, Customer accountHolder) {
        super(accountNumber, balance, accountHolder);
        this.overdraftLimit = 1000.0;
    }


    private boolean isOverdraftExceeded(double amount) {
        return (getBalance() - amount) < -overdraftLimit;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount.");
            return false;
        }
        if (isOverdraftExceeded(amount)) {
            System.out.println("Withdrawal rejected: Overdraft limit exceeded.");
            return false;
        }
        updateBalance(getBalance() - amount);
        System.out.println("Withdrawal successful.");

        return true;
    }

    @Override
    public void displayAccountInfo() {
        System.out.println("AccountNumber = " + this.getAccountNumber());
        System.out.println("Balance = " + this.getBalance());
        System.out.println("CurrentAccount");
    }
}
