package com.learning.banking.repository.mysql;

import com.learning.banking.config.ConnectionProvider;
import com.learning.banking.config.DatabaseConnection;
import com.learning.banking.model.Account;
import com.learning.banking.model.Branch;
import com.learning.banking.model.CurrentAccount;
import com.learning.banking.model.Customer;
import com.learning.banking.model.LimitedAccount;
import com.learning.banking.model.SavingsAccount;
import com.learning.banking.model.StandardAccount;
import com.learning.banking.repository.AccountRepository;
import com.learning.banking.repository.BranchRepository;
import com.learning.banking.repository.CustomerRepository;
import com.learning.banking.repository.DataAccessException;
import com.learning.banking.service.AccountService;
import com.learning.banking.service.TransferService;
import com.learning.banking.service.impl.AccountServiceImpl;
import com.learning.banking.service.impl.TransferServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the REAL database. They are skipped unless the environment variable
 * RUN_DB_TESTS=true is set (plus the usual DB_URL / DB_USER / DB_PASSWORD, or
 * a db.properties file), so a normal "mvn test" never touches your database.
 *
 * Every row these tests create has an ID starting with "IT-". The cleanup
 * below is the ONLY place in the project that physically deletes rows, and
 * it only touches those test rows.
 */
@EnabledIfEnvironmentVariable(named = "RUN_DB_TESTS", matches = "true")
class MySqlRepositoriesIntegrationTest {

    private final ConnectionProvider provider = DatabaseConnection.getInstance();

    private CustomerRepository customers;
    private AccountRepository accounts;
    private BranchRepository branches;
    private Customer owner;

    @BeforeEach
    void setUp() throws SQLException {
        customers = new MySqlCustomerRepository(provider);
        accounts = new MySqlAccountRepository(provider);
        branches = new MySqlBranchRepository(provider);

        deleteTestRows();

        owner = new Customer("IT-CUST-1", "Test Owner", "it-owner@test.com", "pw");
        customers.save(owner);
    }

    @AfterEach
    void tearDown() throws SQLException {
        deleteTestRows();
    }

    // ---------------------------------------------------------------- accounts

    @Test
    void shouldStoreAndReloadEveryAccountTypeWithAllItsFields() {

        accounts.save(new StandardAccount("IT-STD", 300.5, owner));
        accounts.save(new SavingsAccount("IT-SAV", 5_000, owner, 7.5, 250));
        accounts.save(new CurrentAccount("IT-CUR", 800, owner, 1_500));
        accounts.save(new LimitedAccount("IT-LIM", 900, owner, 120));

        assertEquals(300.5, accounts.findById("IT-STD").getBalance());

        SavingsAccount savings = (SavingsAccount) accounts.findById("IT-SAV");
        assertEquals(7.5, savings.getInterestRate());
        assertEquals(250, savings.getMinimumBalance());

        CurrentAccount current = (CurrentAccount) accounts.findById("IT-CUR");
        assertEquals(1_500, current.getOverdraftLimit());

        LimitedAccount limited = (LimitedAccount) accounts.findById("IT-LIM");
        assertEquals(120, limited.getWithdrawalLimit());
        assertEquals("IT-CUST-1", limited.getOwner().getCustomerId());
    }

    @Test
    void shouldReloadAnOverdrawnCurrentAccount() {

        Account current = new CurrentAccount("IT-CUR", 500, owner, 2_000);
        current.withdraw(1_500);               // balance is now -1000
        accounts.save(current);

        Account reloaded = accounts.findById("IT-CUR");

        assertEquals(-1_000.0, reloaded.getBalance());
    }

    @Test
    void saveShouldUpdateAnExistingAccount() {

        Account account = new StandardAccount("IT-STD", 100, owner);
        accounts.save(account);

        account.deposit(50);
        accounts.save(account);

        assertEquals(150, accounts.findById("IT-STD").getBalance());
    }

    @Test
    void deactivateShouldHideTheAccountButKeepEveryColumnInTheDatabase()
            throws SQLException {

        accounts.save(new SavingsAccount("IT-SAV", 5_000, owner, 7.5, 250));

        accounts.deactivateById("IT-SAV");

        assertNull(accounts.findById("IT-SAV"));
        assertFalse(accounts.existsById("IT-SAV"));
        assertTrue(accounts.existsByIdIncludingInactive("IT-SAV"));
        assertTrue(accounts.findAll().stream()
                .noneMatch(a -> a.getAccountId().equals("IT-SAV")));

        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT is_active, deactivated_at, balance, interest_rate, "
                             + "minimum_balance FROM accounts WHERE account_id = ?")) {

            statement.setString(1, "IT-SAV");

            try (ResultSet row = statement.executeQuery()) {
                assertTrue(row.next(), "the row must still exist");
                assertFalse(row.getBoolean("is_active"));
                assertNotNull(row.getTimestamp("deactivated_at"));
                assertEquals(5_000.0, row.getDouble("balance"));
                assertEquals(7.5, row.getDouble("interest_rate"));
                assertEquals(250.0, row.getDouble("minimum_balance"));
            }
        }
    }

    @Test
    void saveShouldRefuseToOverwriteAClosedAccountAndLeaveItUntouched()
            throws SQLException {

        accounts.save(new StandardAccount("IT-STD", 100, owner));
        accounts.deactivateById("IT-STD");

        assertThrows(
                IllegalStateException.class,
                () -> accounts.save(new StandardAccount("IT-STD", 999, owner))
        );

        assertEquals(100.0, readBalanceDirectly("IT-STD"));
    }

    @Test
    void deactivatingAnUnknownAccountShouldFail() {

        assertThrows(
                IllegalArgumentException.class,
                () -> accounts.deactivateById("IT-NOPE")
        );
    }

    @Test
    void saveAllShouldRollBackEverythingWhenOneAccountFails()
            throws SQLException {

        accounts.save(new StandardAccount("IT-A", 100, owner));

        Customer ghost = new Customer("IT-GHOST", "Ghost", "ghost@test.com", "pw");
        // never saved -> the foreign key will reject the second account
        List<Account> batch = List.of(
                new StandardAccount("IT-A", 500, owner),
                new StandardAccount("IT-B", 10, ghost)
        );

        assertThrows(DataAccessException.class, () -> accounts.saveAll(batch));

        assertEquals(100.0, readBalanceDirectly("IT-A"),
                "the first update must have been rolled back");
        assertFalse(accounts.existsByIdIncludingInactive("IT-B"));
    }

    @Test
    void transferShouldMoveMoneyBetweenAccountsInTheDatabase() {

        AccountService accountService =
                new AccountServiceImpl(accounts, customers);
        TransferService transferService = new TransferServiceImpl(accounts);

        accountService.openAccount(new StandardAccount("IT-SRC", 1_000, owner));
        accountService.openAccount(new StandardAccount("IT-DST", 50, owner));

        transferService.transfer("IT-SRC", "IT-DST", 400);

        assertEquals(600, accounts.findById("IT-SRC").getBalance());
        assertEquals(450, accounts.findById("IT-DST").getBalance());
    }

    @Test
    void databaseShouldRejectAnAccountForAnUnknownCustomer() {

        Customer ghost = new Customer("IT-GHOST", "Ghost", "ghost@test.com", "pw");

        assertThrows(
                DataAccessException.class,
                () -> accounts.save(new StandardAccount("IT-X", 10, ghost))
        );
    }

    // ------------------------------------------------- customers and branches

    @Test
    void customerSaveShouldInsertThenUpdate() {

        customers.save(new Customer("IT-CUST-1", "Renamed", "it-owner@test.com", "pw"));

        assertEquals("Renamed", customers.findById("IT-CUST-1").getName());
        assertTrue(customers.existsById("IT-CUST-1"));
        assertNull(customers.findById("IT-MISSING"));
    }

    @Test
    void branchManagerChangeShouldReachTheDatabase() {

        branches.save(new Branch(
                "IT-BR", "Maadi", "Maadi, Cairo", "0100", "it-br@test.com", "Old"));

        Branch branch = branches.findById("IT-BR");
        branch.changeManager("New");
        branches.save(branch);

        assertEquals("New", branches.findById("IT-BR").getManager());
    }

    // ----------------------------------------------------------------- helpers

    private double readBalanceDirectly(String accountId) throws SQLException {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT balance FROM accounts WHERE account_id = ?")) {

            statement.setString(1, accountId);

            try (ResultSet row = statement.executeQuery()) {
                assertTrue(row.next());
                return row.getDouble("balance");
            }
        }
    }

    private void deleteTestRows() throws SQLException {
        try (Connection connection = provider.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("DELETE FROM accounts  WHERE account_id  LIKE 'IT-%'");
            statement.executeUpdate("DELETE FROM customers WHERE customer_id LIKE 'IT-%'");
            statement.executeUpdate("DELETE FROM branches  WHERE branch_id   LIKE 'IT-%'");
        }
    }
}
