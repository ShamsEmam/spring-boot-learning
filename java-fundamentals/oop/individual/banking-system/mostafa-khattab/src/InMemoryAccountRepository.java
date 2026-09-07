import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryAccountRepository implements AccountRepository {
    private final Map<String, Account> accounts = new HashMap<>();

    @Override
    public Account findByNumber(String accountNumber) {
        return accounts.get(accountNumber);
    }

    @Override
    public boolean save(Account account) {
        if (account == null || accounts.containsKey(account.getAccountNumber())) {
            return false;
        }
        accounts.put(account.getAccountNumber(), account);
        return true;
    }

    @Override
    public boolean exists(String accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }
}