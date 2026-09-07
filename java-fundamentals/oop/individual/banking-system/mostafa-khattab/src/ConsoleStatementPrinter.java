public class ConsoleStatementPrinter implements StatementPrinter {
    @Override
    public void printCustomerInfo(Customer customer) {
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }
        System.out.println("========================================");
        System.out.println("Customer Information:");
        System.out.println("ID: " + customer.getCustomerId());
        System.out.println("Name: " + customer.getName());
        System.out.println("Accounts (" + customer.getAccounts().size() + "):");
        for (Account acc : customer.getAccounts()) {
            System.out.println("  - " + acc);
        }
        System.out.println("========================================");
    }

    @Override
    public void printAccountInfo(Account account) {
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        System.out.println("----------------------------------------");
        System.out.println(account);
        System.out.println("----------------------------------------");
    }
}