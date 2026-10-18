
import factory.AccountFactory;
import model.Account;
import model.Customer;
import model.Lookup;
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
import factory.AbstractCustomerFactory;
import factory.RegularCustomerFactory;
import factory.VIPCustomerFactory;
import model.Customer;
import model.CustomerServicePolicy;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        // 1. Initialize repositories
        AccountRepository accountRepo = new JdbcAccountRepository();
        CustomerRepository customerRepo =
                new JdbcCustomerRepository(accountRepo);
        LookupRepository lookupRepo = new JdbcLookupRepository();

        // 2. Display active account types
        System.out.println("=== 1. Active Account Types ===");

        List<Lookup> activeTypes =
                lookupRepo.findActiveByCategory("ACCOUNT_TYPE");

        for (Lookup lookup : activeTypes) {
            System.out.println(
                    "-> " + lookup.getItemName()
                            + " (" + lookup.getItemCode() + ")"
            );
        }

        // 3. Initialize services
        CustomerService customerService =
                new CustomerServiceImpl(customerRepo);

        AccountService accountService =
                new AccountServiceImpl(accountRepo);

        TransferService transferService =
                new FundTransferService(accountService);

        StatementPrinter printer =
                new ConsoleStatementPrinter();

        // 4. Initialize bank
        BankService bank = BankServiceImpl.init(
                "National Tech Bank",
                customerService,
                accountService,
                transferService,
                accountRepo,
                lookupRepo,
                printer
        );

        // 5. Register customer
        Customer mostafa =
                new Customer("CUST001", "Mostafa Khattab");

        bank.registerCustomer(mostafa);

        // 6. Create accounts
        Account savings = AccountFactory.createAccount(
                Account.AccountType.SAVING,
                "ACC-SAV-101",
                5000.0,
                mostafa,
                0.10
        );

        Account current = AccountFactory.createAccount(
                Account.AccountType.CURRENT,
                "ACC-CUR-102",
                1500.0,
                mostafa,
                1000.0
        );

        bank.openAccount(savings);
        bank.openAccount(current);

        // 7. Display initial balances
        System.out.println("\n=== 2. Initial Balances ===");

        printBalance(accountService, "ACC-SAV-101");
        printBalance(accountService, "ACC-CUR-102");

        // 8. Transfer funds
        System.out.println("\n=== 3. Fund Transfer ===");

        boolean transferSuccess = transferService.transfer(
                "ACC-SAV-101",
                "ACC-CUR-102",
                1000.0
        );

        System.out.println(
                "Transfer successful: " + transferSuccess
        );

        System.out.println("\nBalances after transfer:");

        printBalance(accountService, "ACC-SAV-101");
        printBalance(accountService, "ACC-CUR-102");

        // 9. Apply interest
        System.out.println("\n=== 4. Apply Interest ===");

        boolean interestSuccess =
                accountService.applyInterest("ACC-SAV-101");

        System.out.println(
                "Interest applied: " + interestSuccess
        );

        System.out.println("\nBalances after interest:");

        printBalance(accountService, "ACC-SAV-101");
        printBalance(accountService, "ACC-CUR-102");

        // 10. Display final customer statement
        System.out.println(
                "\n=== 5. Final Customer Statement ==="
        );

        bank.showCustomerStatement("CUST001");
    }

    private static void printBalance(
            AccountService accountService,
            String accountNumber) {

        try {
            double balance =
                    accountService.getBalance(accountNumber);

            System.out.printf(
                    "%s balance: %.2f%n",
                    accountNumber,
                    balance
            );

        } catch (IllegalArgumentException e) {
            System.out.println(
                    accountNumber + ": Account not found"
            );
        }


        AbstractCustomerFactory regularFactory =
                new RegularCustomerFactory();

        AbstractCustomerFactory vipFactory =
                new VIPCustomerFactory();

        Customer regularCustomer =
                regularFactory.createCustomer("CUST002", "Ahmed Ali");

        Customer vipCustomer =
                vipFactory.createCustomer("CUST003", "Omar Hassan");

        CustomerServicePolicy regularPolicy =
                regularFactory.createServicePolicy();

        CustomerServicePolicy vipPolicy =
                vipFactory.createServicePolicy();

        System.out.println("=== Regular Customer ===");
        System.out.println(regularCustomer);
        System.out.println("Transfer fee on 1000: "
                + regularPolicy.getTransferFee(1000));
        System.out.println("Daily limit: "
                + regularPolicy.getDailyTransferLimit());

        System.out.println("\n=== VIP Customer ===");
        System.out.println(vipCustomer);
        System.out.println("Transfer fee on 1000: "
                + vipPolicy.getTransferFee(1000));
        System.out.println("Daily limit: "
                + vipPolicy.getDailyTransferLimit());

    }
}
