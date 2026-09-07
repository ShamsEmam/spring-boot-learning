package com.learning.banking;

public class Main {

    public static void main(String[] args) {

        Bank bank = new Bank(
                "Shams Bank",
                "Cairo, Egypt",
                "01012345678"
        );

        Branch branch = new Branch(
                "BR001",
                "Maadi Branch",
                "Maadi, Cairo",
                "01011111111",
                "maadi@shamsbank.com",
                "Ahmed Ali"
        );

        bank.addBranch(branch);

        Customer customer = new Customer(
                "CUST001",
                "Shams",
                "shams@gmail.com",
                "123456"
        );

        bank.addCustomer(customer);

        SavingsAccount savingsAccount =
                new SavingsAccount(
                        "SAV001",
                        10_000.0,
                        customer,
                        5.0,
                        500.0
                );

        CurrentAccount currentAccount =
                new CurrentAccount(
                        "CUR001",
                        5_000.0,
                        customer,
                        2_000.0
                );

        LimitedAccount limitedAccount =
                new LimitedAccount(
                        "LIM001",
                        5_000.0,
                        customer,
                        1_000.0
                );

        bank.addAccount(savingsAccount);
        bank.addAccount(currentAccount);
        bank.addAccount(limitedAccount);

        savingsAccount.addInterest();

        TransferService transferService =
                new BankTransferService();

        transferService.transfer(
                savingsAccount,
                currentAccount,
                1000.0
        );

        System.out.println("===== BANK =====");
        System.out.println(bank.getBankName());

        System.out.println("\n===== ACCOUNTS =====");

        for (Account account : bank.getAccounts()) {

            System.out.println(
                    account.getClass().getSimpleName()
                            + " | "
                            + account.getAccountId()
                            + " | Balance: "
                            + account.getBalance()
            );
        }
    }
}