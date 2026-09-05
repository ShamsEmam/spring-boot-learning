import java.util.ArrayList;
import java.util.List;

public class Customer {

    private final String customerId;
    private String name;
    private List<Account> accounts = new ArrayList<>();

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account acc) {
        if (acc != null) {
            accounts.add(acc);
            System.out.println("The Account Added Successfully");
        } else {
            System.out.println("Invalid account.");
        }
    }

    public void displayCustomerInfo() {
        System.out.println("Customer Id = " + customerId);
        System.out.println("Customer Name = " + name);
        displayAccounts();
    }

    public void displayAccounts() {
        System.out.println("Customer Accounts.");
        for (Account acc : accounts) {
            acc.displayAccountInfo();
        }
    }
    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

}
