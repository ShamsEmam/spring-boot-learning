package com.learning.banking;

public class Main {

    public static void main(String[] args) {

        Bank bank = new Bank(
                "Shams Bank",
                "Cairo, Egypt",
                "01012345678"
        );


        Branch maadiBranch = new Branch(
                "BR001",
                "Maadi Branch",
                "Maadi, Cairo",
                "01011111111",
                "maadi@shamsbank.com",
                "Ahmed Ali"
        );

        Branch nasrCityBranch = new Branch(
                "BR002",
                "Nasr City Branch",
                "Nasr City, Cairo",
                "01022222222",
                "nasrcity@shamsbank.com",
                "Mohamed Hassan"
        );

        bank.addBranch(maadiBranch);
        bank.addBranch(nasrCityBranch);

        Customer customer = new Customer(
                "CUST001",
                "Shams",
                "shams@gmail.com",
                "123456"
        );

        bank.addCustomer(customer);


        SavingsAccount savingsAccount = new SavingsAccount(
                "SAV001",
                10_000.0,
                customer,
                5.0,
                500.0
        );

        CurrentAccount currentAccount = new CurrentAccount(
                "CUR001",
                5_000.0,
                customer,
                2_000.0
        );

        bank.addAccount(savingsAccount);
        bank.addAccount(currentAccount);


        savingsAccount.deposit(2_000.0);
        savingsAccount.addInterest();

        currentAccount.withdraw(6_000.0);

        savingsAccount.transfer(
                currentAccount,
                1_000.0
        );

        System.out.println("========== BANK ==========");
        System.out.println("Name     : " + bank.getBankName());
        System.out.println("Address  : " + bank.getBankAddress());
        System.out.println("Phone    : " + bank.getBankPhone());

        System.out.println("\nCustomers: "
                + bank.getCustomers().size());

        System.out.println("Accounts : "
                + bank.getAccounts().size());

        System.out.println("Branches : "
                + bank.getBranches().size());


        System.out.println("\n========== CUSTOMER ==========");
        System.out.println("ID    : " + customer.getCustomerId());
        System.out.println("Name  : " + customer.getName());
        System.out.println("Email : " + customer.getEmail());


        System.out.println("\n========== ACCOUNTS ==========");

        for (Account account : bank.getAccounts()) {
            printAccount(account);
        }
    }


    private static void printAccount(Account account) {

        System.out.println("\nAccount ID : " + account.getAccountId());
        System.out.println("Owner      : " + account.getOwner().getName());
        System.out.println("Balance    : " + account.getBalance());
    }
}