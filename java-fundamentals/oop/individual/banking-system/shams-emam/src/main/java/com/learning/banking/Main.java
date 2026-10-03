package com.learning.banking;

import com.learning.banking.model.*;
import com.learning.banking.repository.AccountRepository;
import com.learning.banking.repository.BranchRepository;
import com.learning.banking.repository.CustomerRepository;
import com.learning.banking.repository.inmemory.InMemoryAccountRepository;
import com.learning.banking.repository.inmemory.InMemoryBranchRepository;
import com.learning.banking.repository.inmemory.InMemoryCustomerRepository;
import com.learning.banking.service.AccountService;
import com.learning.banking.service.BranchService;
import com.learning.banking.service.CustomerService;
import com.learning.banking.service.TransferService;
import com.learning.banking.service.impl.AccountServiceImpl;
import com.learning.banking.service.impl.BankTransferService;
import com.learning.banking.service.impl.BranchServiceImpl;
import com.learning.banking.service.impl.CustomerServiceImpl;

public class Main {

    public static void main(String[] args) {

        // ==============Bank================

        Bank bank = new Bank(
                "Shams Bank",
                "Cairo, Egypt",
                "01012345678"
        );

        // =============Repositories==============

        CustomerRepository customerRepository =
                new InMemoryCustomerRepository();

        AccountRepository accountRepository =
                new InMemoryAccountRepository();

        BranchRepository branchRepository =
                new InMemoryBranchRepository();

        // ============Services================
        CustomerService customerService =
                new CustomerServiceImpl(
                        customerRepository
                );

        AccountService accountService =
                new AccountServiceImpl(
                        accountRepository,
                        customerRepository
                );

        BranchService branchService =
                new BranchServiceImpl(
                        branchRepository
                );

        TransferService transferService =
                new BankTransferService(
                        accountRepository
                );

        // ===============Customers================

        Customer shams = new Customer(
                "CUST001",
                "Shams",
                "shams@gmail.com",
                "123456"
        );

        Customer ahmed = new Customer(
                "CUST002",
                "Ahmed",
                "ahmed@gmail.com",
                "123456"
        );

        customerService.registerCustomer(shams);
        customerService.registerCustomer(ahmed);

        // ===============Branches==================

        Branch maadiBranch = new Branch(
                "BR001",
                "Maadi Branch",
                "Maadi, Cairo",
                "01098765432",
                "maadi@shamsbank.com",
                "Ahmed Ali"
        );

        Branch nasrCityBranch = new Branch(
                "BR002",
                "Nasr City Branch",
                "Nasr City, Cairo",
                "01011111111",
                "nasrcity@shamsbank.com",
                "Mohamed Hassan"
        );

        branchService.addBranch(maadiBranch);
        branchService.addBranch(nasrCityBranch);

        // ==============Accounts===============

        Account savingsAccount =
                new SavingsAccount(
                        "SAV001",
                        10_000.0,
                        shams,
                        5.0,
                        500.0
                );

        Account currentAccount =
                new CurrentAccount(
                        "CUR001",
                        5_000.0,
                        ahmed,
                        2_000.0
                );

        Account standardAccount =
                new StandardAccount(
                        "STD001",
                        3_000.0,
                        shams
                );

        Account limitedAccount =
                new LimitedAccount(
                        "LIM001",
                        5_000.0,
                        ahmed,
                        1_000.0
                );

        accountService.openAccount(savingsAccount);
        accountService.openAccount(currentAccount);
        accountService.openAccount(standardAccount);
        accountService.openAccount(limitedAccount);

        // ==============Account Operations=================

        accountService.deposit(
                "SAV001",
                2_000.0
        );

        accountService.withdraw(
                "CUR001",
                6_000.0
        );

        // ===============Transfer================

        transferService.transfer(
                "SAV001",
                "CUR001",
                1_000.0
        );

        // ===============Branch Operation================

        branchService.changeBranchManager(
                "BR001",
                "Omar Hassan"
        );

        // =============Output==============

        System.out.println("===== BANK =====");
        System.out.println(
                "Name: " + bank.getBankName()
        );
        System.out.println(
                "Address: " + bank.getBankAddress()
        );
        System.out.println(
                "Phone: " + bank.getBankPhone()
        );

        System.out.println();

        System.out.println("===== ACCOUNTS =====");

        System.out.println(
                "Savings balance: "
                        + savingsAccount.getBalance()
        );

        System.out.println(
                "Current balance: "
                        + currentAccount.getBalance()
        );

        System.out.println(
                "Standard balance: "
                        + standardAccount.getBalance()
        );

        System.out.println(
                "Limited balance: "
                        + limitedAccount.getBalance()
        );

        System.out.println();

        System.out.println("===== BRANCH =====");

        Branch branch =
                branchService.findBranchById("BR001");

        System.out.println(
                "Branch: " + branch.getName()
        );

        System.out.println(
                "Manager: " + branch.getManager()
        );
    }
}