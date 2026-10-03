package repository;

import model.Account;
import java.util.List;

public interface AccountRepository {
    Account findByNumber(String accountNumber);
    boolean save(Account account);
    boolean exists(String accountNumber);
    List<Account> findAll();
    List<Account> findByCustomerId(String customerId);
}