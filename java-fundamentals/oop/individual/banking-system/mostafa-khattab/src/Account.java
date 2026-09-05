public abstract class Account  implements Transferable{

    private final String accountNumber;
    private double balance;
    private Customer accountHolder;

    public Account(String accountNumber, double balance, Customer accountHolder) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountHolder = accountHolder;
    }

    public abstract boolean withdraw(double amount);

    public abstract void displayAccountInfo();

    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be greater than zero.");
            return;
        }
        this.balance += amount;
        System.out.println("Deposit successful. New balance: " + this.balance);
    }

    @Override
    public boolean transfer(Account targetAccount, double amount) {
        if (targetAccount == null) {
            System.out.println("Transfer failed: Destination account does not exist.");
            return false;
        }
        if (targetAccount == this) {
            System.out.println("Transfer failed: Cannot transfer money to the same account.");
            return false;
        }
        if (amount <= 0) {
            System.out.println("Transfer failed: Amount must be greater than zero.");
            return false;
        }

        if (this.withdraw(amount)) {
            targetAccount.deposit(amount);
            System.out.println("Transfer successful: Transferred " + amount + " EGP to account [" + targetAccount.getAccountNumber() + "].");
            return true;
        }

        System.out.println("Transfer failed: Insufficient funds in source account [" + this.accountNumber + "].");
        return false;
    }

    public double getBalance() {
        return balance;
    }

    public Customer getAccountHolder() {
        return accountHolder;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    protected void updateBalance(double newBalance) {
        this.balance = newBalance;
    }
}
