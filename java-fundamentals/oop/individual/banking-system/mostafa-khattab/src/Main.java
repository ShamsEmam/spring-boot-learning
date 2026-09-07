public class Main {
    public static void main(String[] args) {
        CustomerRepository customerRepo = new InMemoryCustomerRepository();
        AccountRepository accountRepo = new InMemoryAccountRepository();
        Transferable transferService = new FundTransferService();
        StatementPrinter printer = new ConsoleStatementPrinter();

        Bank bank = new Bank("National Tech Bank", customerRepo, accountRepo, transferService, printer);

        // 1. إنشاء عميل
        Customer mostafa = new Customer("CUST001", "Mostafa Khattab");
        bank.registerCustomer(mostafa);

        // 2. إنشاء حسابين
        Account savings = new SavingsAccount("ACC-SAV-101", 5000.0, mostafa);
        Account current = new CurrentAccount("ACC-CUR-102", 1500.0, mostafa, 1000.0);

        bank.openAccount(savings);
        bank.openAccount(current);

        // 3. طباعة الحالة قبل التحويل
        System.out.println("BEFORE TRANSFER:");
        bank.showCustomerStatement("CUST001");

        // 4. إجراء عملية تحويل
        boolean success = bank.transferFunds("ACC-SAV-101", "ACC-CUR-102", 1000.0);
        System.out.println("Transfer Success: " + success);

        // 5. طباعة الحالة بعد التحويل
        System.out.println("\nAFTER TRANSFER:");
        bank.showCustomerStatement("CUST001");
    }
}