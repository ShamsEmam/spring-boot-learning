package com.learning.banking.demo;

import com.learning.banking.model.Account;
import com.learning.banking.model.Bank;
import com.learning.banking.model.Branch;
import com.learning.banking.model.CurrentAccount;
import com.learning.banking.model.Customer;
import com.learning.banking.model.LimitedAccount;
import com.learning.banking.model.SavingsAccount;
import com.learning.banking.model.StandardAccount;
import com.learning.banking.service.AccountService;
import com.learning.banking.service.BranchService;
import com.learning.banking.service.CustomerService;
import com.learning.banking.service.TransferService;

/**
 * A runnable story that exercises the whole system.
 *
 * It only uses the SERVICE layer (never repositories), which is the
 * layering rule: presentation -> service -> repository.
 *
 * Every run uses fresh IDs (a run number), so you can run it again and again
 * without clashing with the data already in the database.
 */
public class BankingDemo {

    private final CustomerService customerService;
    private final AccountService accountService;
    private final BranchService branchService;
    private final TransferService transferService;

    public BankingDemo(
            CustomerService customerService,
            AccountService accountService,
            BranchService branchService,
            TransferService transferService) {

        this.customerService = customerService;
        this.accountService = accountService;
        this.branchService = branchService;
        this.transferService = transferService;
    }

    public void run() {
        String runId = String.valueOf(System.currentTimeMillis() % 10_000_000);

        Bank bank = new Bank("Shams Bank", "Cairo, Egypt", "01012345678");

        Customer shams = new Customer(
                "CUST-S-" + runId, "Shams",
                "shams." + runId + "@gmail.com", "123456");
        Customer ahmed = new Customer(
                "CUST-A-" + runId, "Ahmed",
                "ahmed." + runId + "@gmail.com", "123456");

        String savingsId = "SAV-" + runId;
        String currentId = "CUR-" + runId;
        String standardId = "STD-" + runId;
        String limitedId = "LIM-" + runId;
        String temporaryId = "TMP-" + runId;
        String branchId = "BR-" + runId;

        registerCustomers(shams, ahmed);
        addBranch(branchId);
        openAccounts(shams, ahmed, savingsId, currentId, standardId, limitedId, temporaryId);

        runOperations(savingsId, currentId);
        changeManager(branchId);
        closeAccounts(savingsId, temporaryId);

        printSummary(bank, savingsId, currentId, standardId, limitedId, branchId);
    }

    private void registerCustomers(Customer... customers) {
        for (Customer customer : customers) {
            customerService.registerCustomer(customer);
        }
    }

    private void addBranch(String branchId) {
        branchService.addBranch(new Branch(
                branchId, "Maadi Branch", "Maadi, Cairo",
                "01098765432", "maadi@shamsbank.com", "Ahmed Ali"));
    }

    private void openAccounts(
            Customer shams, Customer ahmed,
            String savingsId, String currentId, String standardId,
            String limitedId, String temporaryId) {

        accountService.openAccount(
                new SavingsAccount(savingsId, 10_000.0, shams, 5.0, 500.0));
        accountService.openAccount(
                new CurrentAccount(currentId, 5_000.0, ahmed, 2_000.0));
        accountService.openAccount(
                new StandardAccount(standardId, 3_000.0, shams));
        accountService.openAccount(
                new LimitedAccount(limitedId, 5_000.0, ahmed, 1_000.0));
        accountService.openAccount(
                new StandardAccount(temporaryId, 0.0, shams));
    }

    private void runOperations(String savingsId, String currentId) {
        System.out.println("===== OPERATIONS =====");

        accountService.deposit(savingsId, 2_000.0);

        accountService.withdraw(currentId, 6_000.0);
        System.out.println("Current account after overdraft (read back from DB): "
                + accountService.findAccountById(currentId).getBalance());

        transferService.transfer(savingsId, currentId, 1_000.0);
        System.out.println("Transferred 1000.0 from savings to current");
        System.out.println();
    }

    private void changeManager(String branchId) {
        branchService.changeBranchManager(branchId, "Omar Hassan");
    }

    private void closeAccounts(String savingsId, String temporaryId) {
        System.out.println("===== SOFT DELETE =====");

        try {
            accountService.closeAccount(savingsId);
        } catch (IllegalStateException e) {
            System.out.println("Cannot close " + savingsId + ": " + e.getMessage());
        }

        accountService.closeAccount(temporaryId);
        System.out.println("Closed " + temporaryId
                + " (is_active = false, the row stays in the database)");

        try {
            accountService.findAccountById(temporaryId);
        } catch (IllegalArgumentException e) {
            System.out.println("Looking it up now says: " + e.getMessage());
        }
        System.out.println();
    }

    private void printSummary(
            Bank bank, String savingsId, String currentId,
            String standardId, String limitedId, String branchId) {

        System.out.println("===== BANK =====");
        System.out.println("Name: " + bank.getBankName());
        System.out.println("Address: " + bank.getBankAddress());
        System.out.println("Phone: " + bank.getBankPhone());
        System.out.println();

        System.out.println("===== ACCOUNTS (read back from the database) =====");
        printBalance("Savings", accountService.findAccountById(savingsId));
        printBalance("Current", accountService.findAccountById(currentId));
        printBalance("Standard", accountService.findAccountById(standardId));
        printBalance("Limited", accountService.findAccountById(limitedId));
        System.out.println();

        Branch branch = branchService.findBranchById(branchId);
        System.out.println("===== BRANCH =====");
        System.out.println("Branch: " + branch.getName());
        System.out.println("Manager: " + branch.getManager());
    }

    private void printBalance(String label, Account account) {
        System.out.println(label + " balance: " + account.getBalance());
    }
}
