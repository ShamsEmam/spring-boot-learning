import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Customer {
    private final String customerId;
    private String name;
    private final List<Account> accounts;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
        this.accounts = new ArrayList<>();
    }

    public boolean addAccount(Account acc) {
        if (acc != null && !accounts.contains(acc)) {
            accounts.add(acc);
            return true;
        }
        return false;
    }

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }

    @Override
    public String toString() {
        return String.format("Customer[ID=%s, Name=%s, AccountsCount=%d]", customerId, name, accounts.size());
    }
}