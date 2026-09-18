package service.impl;

import model.Account;
import model.Customer;
import repository.AccountRepository;
import service.AccountService;
import service.BankService;
import service.CustomerService;
import service.TransferService;
import view.StatementPrinter;

public class BankServiceImpl implements BankService {

    private static volatile BankServiceImpl instance;

    private final String name;
    private final CustomerService customerService;
    private final AccountService accountService;
    private final TransferService transferService;
    private final AccountRepository accountRepo;
    private final StatementPrinter printer;

    private BankServiceImpl(String name,
                            CustomerService customerService,
                            AccountService accountService,
                            TransferService transferService,
                            AccountRepository accountRepo,
                            StatementPrinter printer) {
        this.name = name;
        this.customerService = customerService;
        this.accountService = accountService;
        this.transferService = transferService;
        this.accountRepo = accountRepo;
        this.printer = printer;
    }

    public static BankServiceImpl init(String name,
                                       CustomerService customerService,
                                       AccountService accountService,
                                       TransferService transferService,
                                       AccountRepository accountRepo,
                                       StatementPrinter printer) {
        if (instance == null) {
            synchronized (BankServiceImpl.class) {
                if (instance == null) {
                    instance = new BankServiceImpl(name, customerService, accountService, transferService, accountRepo, printer);
                }
            }
        }
        return instance;
    }

    public static BankServiceImpl getInstance() {
        if (instance == null) {
            throw new IllegalStateException("BankServiceImpl is not initialized yet. Call init() first.");
        }
        return instance;
    }

    @Override
    public boolean registerCustomer(Customer customer) {
        return customerService.registerCustomer(customer);
    }

    @Override
    public boolean openAccount(Account account) {
        if (account == null) {
            return false;
        }

        Customer holder = account.getAccountHolder();
        if (holder == null || !customerService.customerExists(holder.getCustomerId())) {
            return false;
        }

        if (accountRepo.save(account)) {
            return customerService.addAccountToCustomer(holder.getCustomerId(), account);
        }
        return false;
    }

    @Override
    public boolean transferFunds(String fromAccountNumber, String toAccountNumber, double amount) {
        return transferService.transfer(fromAccountNumber, toAccountNumber, amount);
    }

    @Override
    public boolean deposit(String accountNumber, double amount) {
        return accountService.deposit(accountNumber, amount);
    }

    @Override
    public boolean withdraw(String accountNumber, double amount) {
        return accountService.withdraw(accountNumber, amount);
    }

    @Override
    public boolean applyInterest(String accountNumber) {
        return accountService.applyInterest(accountNumber);
    }

    @Override
    public void showCustomerStatement(String customerId) {
        Customer customer = customerService.getCustomer(customerId);
        printer.printCustomerInfo(customer);
    }

    @Override
    public void showAccountStatement(String accountNumber) {
        Account account = accountService.getAccount(accountNumber);
        printer.printAccountInfo(account);
    }

    @Override
    public String getName() {
        return name;
    }
}
