import model.Account;
import model.CurrentAccount;
import model.Customer;
import model.SavingsAccount;
import repository.AccountRepository;
import repository.CustomerRepository;
import repository.jdbc.JdbcAccountRepository;
import repository.jdbc.JdbcCustomerRepository;
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

public class Main {
    public static void main(String[] args) {
        String dbUrl = "jdbc:postgresql://localhost:5432/bank_db";
        String dbUser = "postgres";
        String dbPassword = "12345";

        AccountRepository accountRepo = new JdbcAccountRepository(dbUrl, dbUser, dbPassword);
        CustomerRepository customerRepo = new JdbcCustomerRepository(dbUrl, dbUser, dbPassword, accountRepo);

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
                printer
        );

        Customer mostafa = new Customer("CUST001", "Mostafa Khattab");
        bank.registerCustomer(mostafa);

        Account savings = new SavingsAccount("ACC-SAV-101", 5000.0, mostafa, 0.10);
        Account current = new CurrentAccount("ACC-CUR-102", 1500.0, mostafa, 1000.0);

        bank.openAccount(savings);
        bank.openAccount(current);

        System.out.println("=== الحالة المبدئية ===");
        bank.showCustomerStatement("CUST001");

        bank.transferFunds("ACC-SAV-101", "ACC-CUR-102", 1000.0);
        bank.applyInterest("ACC-SAV-101");

        System.out.println("\n=== الحالة بعد العمليات والفائدة ===");
        bank.showCustomerStatement("CUST001");
    }
}