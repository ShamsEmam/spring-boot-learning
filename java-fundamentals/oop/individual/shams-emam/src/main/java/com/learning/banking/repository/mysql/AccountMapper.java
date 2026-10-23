package com.learning.banking.repository.mysql;

import com.learning.banking.model.Account;
import com.learning.banking.model.AccountType;
import com.learning.banking.model.CurrentAccount;
import com.learning.banking.model.Customer;
import com.learning.banking.model.LimitedAccount;
import com.learning.banking.model.SavingsAccount;
import com.learning.banking.model.StandardAccount;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Translates between an Account object and a row of the "accounts" table.
 *
 * Single responsibility: ONLY mapping. It runs no SQL, so the repository
 * stays small. If a new account type is added, this is the one class
 * (besides the new account itself) that must learn about it.
 */
final class AccountMapper {

    private AccountMapper() {
    }

    /**
     * Fills the 7 data columns, in this order:
     * type, balance, customer, interest rate, minimum balance,
     * overdraft limit, withdrawal limit.
     * Columns that do not belong to the account's type are set to NULL.
     */
    static void bindColumns(PreparedStatement statement, Account account)
            throws SQLException {

        statement.setString(1, account.getType().name());
        statement.setDouble(2, account.getBalance());
        statement.setString(3, account.getOwner().getCustomerId());

        setNullableDouble(statement, 4, interestRateOf(account));
        setNullableDouble(statement, 5, minimumBalanceOf(account));
        setNullableDouble(statement, 6, overdraftLimitOf(account));
        setNullableDouble(statement, 7, withdrawalLimitOf(account));
    }

    /** Builds an Account (with its owner) from one row of the joined query. */
    static Account toAccount(ResultSet row) throws SQLException {
        Customer owner = new Customer(
                row.getString("customer_id"),
                row.getString("customer_name"),
                row.getString("customer_email"),
                row.getString("customer_password")
        );

        String accountId = row.getString("account_id");
        double balance = row.getDouble("balance");
        AccountType type = AccountType.valueOf(row.getString("account_type"));

        return switch (type) {
            case STANDARD -> new StandardAccount(accountId, balance, owner);
            case SAVINGS -> new SavingsAccount(
                    accountId, balance, owner,
                    row.getDouble("interest_rate"),
                    row.getDouble("minimum_balance"));
            case CURRENT -> CurrentAccount.restore(
                    accountId, balance, owner,
                    row.getDouble("overdraft_limit"));
            case LIMITED -> new LimitedAccount(
                    accountId, balance, owner,
                    row.getDouble("withdrawal_limit"));
        };
    }

    private static Double interestRateOf(Account account) {
        return account instanceof SavingsAccount savings
                ? savings.getInterestRate() : null;
    }

    private static Double minimumBalanceOf(Account account) {
        return account instanceof SavingsAccount savings
                ? savings.getMinimumBalance() : null;
    }

    private static Double overdraftLimitOf(Account account) {
        return account instanceof CurrentAccount current
                ? current.getOverdraftLimit() : null;
    }

    private static Double withdrawalLimitOf(Account account) {
        return account instanceof LimitedAccount limited
                ? limited.getWithdrawalLimit() : null;
    }

    private static void setNullableDouble(
            PreparedStatement statement,
            int index,
            Double value) throws SQLException {

        if (value == null) {
            statement.setNull(index, Types.DECIMAL);
        } else {
            statement.setDouble(index, value);
        }
    }
}
