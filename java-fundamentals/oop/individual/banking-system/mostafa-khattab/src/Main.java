public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank("Misr International Bank");

        Customer ahmed = new Customer("C100", "Ahmed");
        Customer omar = new Customer("C200", "Omar");

        bank.addCustomer(ahmed);
        bank.addCustomer(omar);
        System.out.println();

        Account ahmedSavings = new SavingsAccount("SA-5001", 5000.0, ahmed);
        Account omarCurrent = new CurrentAccount("CA-6001", 3000.0, omar);

        bank.openAccount(ahmedSavings);
        bank.openAccount(omarCurrent);

        System.out.println("\n=== Core Acceptance Scenario ===");

        System.out.println("\n[1] Ahmed deposits 1000 EGP:");
        ahmedSavings.deposit(1000.0);

        System.out.println("\n[2] Omar withdraws 500 EGP:");
        omarCurrent.withdraw(500.0);

        System.out.println("\n[3] Ahmed transfers 1500 EGP to Omar:");
        ahmedSavings.transfer(omarCurrent, 1500.0);

        System.out.println("\n=== Final Verification ===");
        System.out.println("Ahmed Expected: 4500.0 EGP | Actual: " + ahmedSavings.getBalance() + " EGP");
        System.out.println("Omar Expected : 4000.0 EGP | Actual: " + omarCurrent.getBalance() + " EGP");

        System.out.println("\n=== Testing Invalid Operations ===");

        System.out.println("\n[Invalid 1] Withdrawing excess amount from Savings:");
        ahmedSavings.withdraw(10000.0);

        System.out.println("\n[Invalid 2] Negative deposit:");
        ahmedSavings.deposit(-50.0);

        System.out.println("\n[Invalid 3] Transfer exceeding balance:");
        ahmedSavings.transfer(omarCurrent, 20000.0);
    }
}