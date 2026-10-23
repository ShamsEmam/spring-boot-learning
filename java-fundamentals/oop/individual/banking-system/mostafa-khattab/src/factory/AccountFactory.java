package factory;

import model.Account;
import model.Account.AccountType;
import model.CurrentAccountStrategy;
import model.Customer;
import model.SavingAccountStrategy;

import java.util.EnumMap;
import java.util.Map;

public class AccountFactory {

    @FunctionalInterface
    public interface AccountCreator {
        Account create(String accountNumber, double balance, Customer holder, double param);
    }

    private static final Map<AccountType, AccountCreator> registry = new EnumMap<>(AccountType.class);

    static {
        registerAccountType(AccountType.SAVING, (accNum, bal, holder, rate) ->
                new Account(
                        accNum, bal, holder,
                        AccountType.SAVING, 0.0, rate,
                        new SavingAccountStrategy()
                ));

        registerAccountType(AccountType.CURRENT, (accNum, bal, holder, limit) ->
                new Account(
                        accNum, bal, holder,
                        AccountType.CURRENT, limit, 0.0,
                        new CurrentAccountStrategy()
                ));
    }

    public static void registerAccountType(AccountType type, AccountCreator creator) {
        registry.put(type, creator);
    }

    public static Account createAccount(AccountType type, String accountNumber, double balance, Customer holder, double param) {
        AccountCreator creator = registry.get(type);
        if (creator == null) {
            throw new IllegalArgumentException("Unsupported or unregistered account type: " + type);
        }
        return creator.create(accountNumber, balance, holder, param);
    }
}