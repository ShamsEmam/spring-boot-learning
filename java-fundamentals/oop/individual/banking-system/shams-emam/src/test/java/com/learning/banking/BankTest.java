package com.learning.banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {

    private Bank bank;
    private Customer customer;
    private SavingsAccount savingsAccount;
    private Branch branch;

    @BeforeEach
    void setUp() {

        bank = new Bank(
                "Shams Bank",
                "Maadi, Cairo",
                "01012345678"
        );

        customer = new Customer(
                "CUST001",
                "Shams",
                "shams@gmail.com",
                "123456"
        );

        savingsAccount = new SavingsAccount(
                "SAV001",
                10_000.0,
                customer,
                5.0,
                500.0
        );

        branch = new Branch(
                "BR001",
                "Maadi Branch",
                "Maadi, Cairo",
                "01098765432",
                "maadi@shamsbank.com",
                "Ahmed Ali"
        );
    }

    // ==========================================
    // Constructor Tests
    // ==========================================

    @Test
    void constructorShouldCreateBankSuccessfully() {

        assertEquals("Shams Bank", bank.getBankName());
        assertEquals("Maadi, Cairo", bank.getBankAddress());
        assertEquals("01012345678", bank.getBankPhone());

        assertTrue(bank.getCustomers().isEmpty());
        assertTrue(bank.getAccounts().isEmpty());
        assertTrue(bank.getBranches().isEmpty());
    }

    @Test
    void constructorShouldRejectNullBankName() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Bank(
                        null,
                        "Maadi, Cairo",
                        "01012345678"
                )
        );
    }

    @Test
    void constructorShouldRejectBlankBankName() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Bank(
                        "   ",
                        "Maadi, Cairo",
                        "01012345678"
                )
        );
    }

    @Test
    void constructorShouldRejectNullAddress() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Bank(
                        "Shams Bank",
                        null,
                        "01012345678"
                )
        );
    }

    @Test
    void constructorShouldRejectBlankAddress() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Bank(
                        "Shams Bank",
                        "   ",
                        "01012345678"
                )
        );
    }

    @Test
    void constructorShouldRejectNullPhone() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Bank(
                        "Shams Bank",
                        "Maadi, Cairo",
                        null
                )
        );
    }

    @Test
    void constructorShouldRejectBlankPhone() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Bank(
                        "Shams Bank",
                        "Maadi, Cairo",
                        "   "
                )
        );
    }

    // =============Customer Tests=================

    @Test
    void addCustomerShouldAddCustomerSuccessfully() {

        bank.addCustomer(customer);

        assertEquals(
                1,
                bank.getCustomers().size()
        );

        assertTrue(
                bank.getCustomers().contains(customer)
        );
    }

    @Test
    void addCustomerShouldRejectNullCustomer() {

        assertThrows(
                IllegalArgumentException.class,
                () -> bank.addCustomer(null)
        );

        assertTrue(bank.getCustomers().isEmpty());
    }

    @Test
    void addCustomerShouldRejectDuplicateCustomerId() {

        Customer duplicateCustomer = new Customer(
                "CUST001",
                "Ahmed",
                "ahmed@gmail.com",
                "123456"
        );

        bank.addCustomer(customer);

        assertThrows(
                IllegalArgumentException.class,
                () -> bank.addCustomer(duplicateCustomer)
        );

        assertEquals(
                1,
                bank.getCustomers().size()
        );
    }

    @Test
    void findCustomerByIdShouldReturnCustomer() {

        bank.addCustomer(customer);

        Customer foundCustomer =
                bank.findCustomerById("CUST001");

        assertEquals(
                customer,
                foundCustomer
        );
    }

    @Test
    void findCustomerByIdShouldReturnNullWhenCustomerDoesNotExist() {

        Customer foundCustomer =
                bank.findCustomerById("CUST999");

        assertNull(foundCustomer);
    }

    @Test
    void findCustomerByIdShouldRejectNullId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> bank.findCustomerById(null)
        );
    }

    @Test
    void findCustomerByIdShouldRejectBlankId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> bank.findCustomerById("   ")
        );
    }

    // ============Account Tests================

    @Test
    void addAccountShouldAddAccountSuccessfully() {

        bank.addCustomer(customer);

        bank.addAccount(savingsAccount);

        assertEquals(
                1,
                bank.getAccounts().size()
        );

        assertTrue(
                bank.getAccounts().contains(savingsAccount)
        );
    }

    @Test
    void addAccountShouldRejectNullAccount() {

        assertThrows(
                IllegalArgumentException.class,
                () -> bank.addAccount(null)
        );

        assertTrue(bank.getAccounts().isEmpty());
    }

    @Test
    void addAccountShouldRejectDuplicateAccountId() {

        bank.addCustomer(customer);

        Account duplicateAccount = new CurrentAccount(
                "SAV001",
                5_000.0,
                customer,
                2_000.0
        );

        bank.addAccount(savingsAccount);

        assertThrows(
                IllegalArgumentException.class,
                () -> bank.addAccount(duplicateAccount)
        );

        assertEquals(
                1,
                bank.getAccounts().size()
        );
    }

    @Test
    void addAccountShouldRejectAccountWhoseOwnerIsNotRegistered() {

        assertThrows(
                IllegalStateException.class,
                () -> bank.addAccount(savingsAccount)
        );

        assertTrue(bank.getAccounts().isEmpty());
    }

    @Test
    void findAccountByIdShouldReturnAccount() {

        bank.addCustomer(customer);
        bank.addAccount(savingsAccount);

        Account foundAccount =
                bank.findAccountById("SAV001");

        assertEquals(
                savingsAccount,
                foundAccount
        );
    }

    @Test
    void findAccountByIdShouldReturnNullWhenAccountDoesNotExist() {

        Account foundAccount =
                bank.findAccountById("ACC999");

        assertNull(foundAccount);
    }

    @Test
    void findAccountByIdShouldRejectNullId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> bank.findAccountById(null)
        );
    }

    @Test
    void findAccountByIdShouldRejectBlankId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> bank.findAccountById("   ")
        );
    }

    // ============ Branch Tests=================

    @Test
    void addBranchShouldAddBranchSuccessfully() {

        bank.addBranch(branch);

        assertEquals(
                1,
                bank.getBranches().size()
        );

        assertTrue(
                bank.getBranches().contains(branch)
        );
    }

    @Test
    void addBranchShouldRejectNullBranch() {

        assertThrows(
                IllegalArgumentException.class,
                () -> bank.addBranch(null)
        );

        assertTrue(bank.getBranches().isEmpty());
    }

    @Test
    void addBranchShouldRejectDuplicateBranchId() {

        Branch duplicateBranch = new Branch(
                "BR001",
                "Nasr City Branch",
                "Nasr City, Cairo",
                "01011111111",
                "nasrcity@shamsbank.com",
                "Mohamed Hassan"
        );

        bank.addBranch(branch);

        assertThrows(
                IllegalArgumentException.class,
                () -> bank.addBranch(duplicateBranch)
        );

        assertEquals(
                1,
                bank.getBranches().size()
        );
    }

    @Test
    void findBranchByIdShouldReturnBranch() {

        bank.addBranch(branch);

        Branch foundBranch =
                bank.findBranchById("BR001");

        assertEquals(
                branch,
                foundBranch
        );
    }

    @Test
    void findBranchByIdShouldReturnNullWhenBranchDoesNotExist() {

        Branch foundBranch =
                bank.findBranchById("BR999");

        assertNull(foundBranch);
    }

    @Test
    void findBranchByIdShouldRejectNullId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> bank.findBranchById(null)
        );
    }

    @Test
    void findBranchByIdShouldRejectBlankId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> bank.findBranchById("   ")
        );
    }

    // ===========Encapsulation Tests===============

    @Test
    void customersListShouldBeReadOnly() {

        bank.addCustomer(customer);

        List<Customer> customers =
                bank.getCustomers();

        assertThrows(
                UnsupportedOperationException.class,
                () -> customers.add(
                        new Customer(
                                "CUST002",
                                "Ahmed",
                                "ahmed@gmail.com",
                                "123456"
                        )
                )
        );

        assertEquals(
                1,
                bank.getCustomers().size()
        );
    }

    @Test
    void accountsListShouldBeReadOnly() {

        bank.addCustomer(customer);
        bank.addAccount(savingsAccount);

        List<Account> accounts =
                bank.getAccounts();

        assertThrows(
                UnsupportedOperationException.class,
                () -> accounts.clear()
        );

        assertEquals(
                1,
                bank.getAccounts().size()
        );
    }

    @Test
    void branchesListShouldBeReadOnly() {

        bank.addBranch(branch);

        List<Branch> branches =
                bank.getBranches();

        assertThrows(
                UnsupportedOperationException.class,
                () -> branches.clear()
        );

        assertEquals(
                1,
                bank.getBranches().size()
        );
    }
}