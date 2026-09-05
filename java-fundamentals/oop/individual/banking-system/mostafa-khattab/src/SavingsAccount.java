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
            System.out.println("Interest of " + interest + " EGP applied successfully.");
        }
    }


    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Please Enter A Valid Number");
            return false;
        }

        double balance = this.getBalance();

        if (amount > balance) {
            System.out.println("You Can`t Withdraw This Number");
        } else {
            this.updateBalance(balance - amount);
            System.out.println("Withdraw done.....");
        }
        return true;
    }

    @Override
    public void displayAccountInfo() {
        System.out.println("AccountNumber = " + this.getAccountNumber());
        System.out.println("Balance = " + this.getBalance());
        System.out.println("SavingAccount");
    }
}
