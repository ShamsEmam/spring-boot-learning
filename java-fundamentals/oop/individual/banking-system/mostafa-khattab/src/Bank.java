import java.util.ArrayList;
import java.util.List;

public class Bank {
    private final String name;
    private final List<Customer> customers;
    private final List<Account> accounts;

    public Bank(String name) {
        this.name = name;
        this.customers = new ArrayList<>();
        this.accounts = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        if (customer != null && getCustomerById(customer.getCustomerId()) == null) {
            customers.add(customer);
            System.out.println("Customer [" + customer.getName() + "] registered successfully.");
        } else {
            System.out.println("Failed to register customer: Invalid or already exists.");
        }
    }

    public void openAccount(Account account) {
        if (account == null) {
            System.out.println("Failed to open account: Account cannot be null.");
            return;
        }

        if (getAccountByNumber(account.getAccountNumber()) != null) {
            System.out.println("Failed to open account: Account number already exists.");
            return;
        }

        Customer holder = account.getAccountHolder();
        if (holder == null || getCustomerById(holder.getCustomerId()) == null) {
            System.out.println("Failed to open account: Every account must belong to a registered customer.");
            return;
        }

        accounts.add(account);
        holder.addAccount(account);
        System.out.println("Account [" + account.getAccountNumber() + "]" + " opened successfully for [" + holder.getName() + "].");
    }

    public Customer getCustomerById(String customerId) {
        for (Customer c : customers) {
            if (c.getCustomerId().equals(customerId)) {
                return c;
            }
        }
        return null;
    }

    public Account getAccountByNumber(String accountNumber) {
        for (Account a : accounts) {
            if (a.getAccountNumber().equals(accountNumber)) {
                return a;
            }
        }
        return null;
    }

    public boolean transferFunds(String fromAccountNumber, String toAccountNumber, double amount) {
        Account source = getAccountByNumber(fromAccountNumber);
        Account destination = getAccountByNumber(toAccountNumber);

        if (source == null) {
            System.out.println("Bank transfer failed: Source account not found.");
            return false;
        }
        if (destination == null) {
            System.out.println("Bank transfer failed: Destination account not found.");
            return false;
        }

        return source.transfer(destination, amount);
    }
}