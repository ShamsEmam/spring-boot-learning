import model.Account;
import model.CurrentAccount;
import model.Customer;
import model.Lookup;
import model.SavingsAccount;
import repository.AccountRepository;
import repository.CustomerRepository;
import repository.LookupRepository;
import repository.jdbc.JdbcAccountRepository;
import repository.jdbc.JdbcCustomerRepository;
import repository.jdbc.JdbcLookupRepository;
import service.AccountService;
import service.BankService;
import service.CustomerService;
import service.TransferService;
import service.impl.AccountServiceImpl;
import service.impl.BankServiceImpl;
import service.impl.CustomerServiceImpl;
import service.impl.FundTransferService;
import view.StatementPrinter;
import view.impl.ConsoleStatementPrinter;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String dbUrl = "jdbc:postgresql://localhost:5432/bank_db";
        String dbUser = "postgres";
        String dbPassword = "12345";

        AccountRepository accountRepo = new JdbcAccountRepository(dbUrl, dbUser, dbPassword);
        CustomerRepository customerRepo = new JdbcCustomerRepository(dbUrl, dbUser, dbPassword, accountRepo);
        LookupRepository lookupRepo = new JdbcLookupRepository(dbUrl, dbUser, dbPassword);

        System.out.println("=== 1. Active Account Types in System (Active Lookups) ===");
        List<Lookup> activeTypes = lookupRepo.findActiveByCategory("ACCOUNT_TYPE");
        for (Lookup l : activeTypes) {
            System.out.println("-> " + l.getItemName() + " (" + l.getItemCode() + ")");
        }

        CustomerService customerService = new CustomerServiceImpl(customerRepo);
        AccountService accountService = new AccountServiceImpl(accountRepo);
        TransferService transferService = new FundTransferService(accountService);
        StatementPrinter printer = new ConsoleStatementPrinter();

        BankService bank = BankServiceImpl.init(
                "National Tech Bank",
                customerService,
                accountService,
                transferService,
                accountRepo,
                lookupRepo,
                printer
        );

        Customer mostafa = new Customer("CUST001", "Mostafa Khattab");
        bank.registerCustomer(mostafa);

        Account savings = new SavingsAccount("ACC-SAV-101", 5000.0, mostafa, 0.10);
        Account current = new CurrentAccount("ACC-CUR-102", 1500.0, mostafa, 1000.0);

        bank.openAccount(savings);
        bank.openAccount(current);

        System.out.println("\n=== 2. Initial Customer Account Statement ===");
        bank.showCustomerStatement("CUST001");

        bank.transferFunds("ACC-SAV-101", "ACC-CUR-102", 1000.0);
        bank.applyInterest("ACC-SAV-101");

        System.out.println("\n=== 3. Account Statement After Transfer and Interest ===");
        bank.showCustomerStatement("CUST001");

        System.out.println("\n=== 4. Soft Delete Test for CURRENT Account ===");
        lookupRepo.softDelete("ACCOUNT_TYPE", "CURRENT");

        System.out.println("Available Account Types After Soft Delete:");
        List<Lookup> updatedTypes = lookupRepo.findActiveByCategory("ACCOUNT_TYPE");
        for (Lookup l : updatedTypes) {
            System.out.println("-> " + l.getItemName() + " (" + l.getItemCode() + ")");
        }

        System.out.println("\nAttempting to open a new CURRENT account after inactivation:");
        Account newCurrent = new CurrentAccount("ACC-CUR-103", 500.0, mostafa, 500.0);
        boolean opened = bank.openAccount(newCurrent);
        System.out.println("Was the new account opened? " + opened);
    }
}