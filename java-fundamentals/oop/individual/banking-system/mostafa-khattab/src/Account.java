import java.util.Objects;

public abstract class Account {
    private final String accountNumber;
    private double balance;
    private final Customer accountHolder;

    public Account(String accountNumber, double balance, Customer accountHolder) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountHolder = accountHolder;
    }

    public abstract boolean withdraw(double amount);

    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        this.balance += amount;
        return true;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    @Override
    public String toString() {
        return String.format("Account[%s] | Balance: %.2f | Holder: %s",
                accountNumber, balance, accountHolder.getName());
    }
}