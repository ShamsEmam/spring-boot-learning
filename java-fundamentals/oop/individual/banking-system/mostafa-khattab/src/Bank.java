public class Bank {
    private final String name;
    private final CustomerRepository customerRepo;
    private final AccountRepository accountRepo;
    private final Transferable transferService;
    private final StatementPrinter printer;

    public Bank(String name,
                CustomerRepository customerRepo,
                AccountRepository accountRepo,
                Transferable transferService,
                StatementPrinter printer) {
        this.name = name;
        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
        this.transferService = transferService;
        this.printer = printer;
    }

    public boolean registerCustomer(Customer customer) {
        if (customer == null) return false;
        return customerRepo.save(customer);
    }

    public boolean openAccount(Account account) {
        if (account == null) return false;

        Customer holder = account.getAccountHolder();
        if (holder == null || !customerRepo.exists(holder.getCustomerId())) {
            return false;
        }

        if (accountRepo.save(account)) {
            holder.addAccount(account);
            return true;
        }
        return false;
    }

    public boolean transferFunds(String fromAccountNumber, String toAccountNumber, double amount) {
        Account source = accountRepo.findByNumber(fromAccountNumber);
        Account destination = accountRepo.findByNumber(toAccountNumber);

        return transferService.transfer(source, destination, amount);
    }

    public void showCustomerStatement(String customerId) {
        Customer customer = customerRepo.findById(customerId);
        printer.printCustomerInfo(customer);
    }

    public void showAccountStatement(String accountNumber) {
        Account account = accountRepo.findByNumber(accountNumber);
        printer.printAccountInfo(account);
    }

    public String getName() {
        return name;
    }

    public CustomerRepository getCustomerRepo() {
        return customerRepo;
    }

    public AccountRepository getAccountRepo() {
        return accountRepo;
    }
}