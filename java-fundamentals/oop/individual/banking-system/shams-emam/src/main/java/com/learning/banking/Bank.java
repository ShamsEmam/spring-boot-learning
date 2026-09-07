package com.learning.banking;

import java.util.ArrayList;
import java.util.List;

public class Bank {

    private final String bankName;
    private final String bankAddress;
    private final String bankPhone;

    private final List<Customer> customers;
    private final List<Account> accounts;
    private final List<Branch> branches;

    public Bank(
            String bankName,
            String bankAddress,
            String bankPhone) {

        validateRequiredField(bankName, "Bank name");
        validateRequiredField(bankAddress, "Bank address");
        validateRequiredField(bankPhone, "Bank phone");

        this.bankName = bankName;
        this.bankAddress = bankAddress;
        this.bankPhone = bankPhone;

        customers = new ArrayList<>();
        accounts = new ArrayList<>();
        branches = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer cannot be null"
            );
        }

        if (findCustomerById(customer.getCustomerId()) != null) {
            throw new IllegalArgumentException(
                    "Customer ID already exists"
            );
        }

        customers.add(customer);
    }

    public Customer findCustomerById(String customerId) {

        validateRequiredField(customerId, "Customer ID");

        for (Customer customer : customers) {

            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }

        return null;
    }

    public void addAccount(Account account) {

        if (account == null) {
            throw new IllegalArgumentException(
                    "Account cannot be null"
            );
        }

        if (findAccountById(account.getAccountId()) != null) {
            throw new IllegalArgumentException(
                    "Account ID already exists"
            );
        }

        Customer registeredOwner =
                findCustomerById(
                        account.getOwner().getCustomerId()
                );

        if (registeredOwner == null) {
            throw new IllegalStateException(
                    "Account owner must be registered in the bank"
            );
        }

        accounts.add(account);
    }

    public Account findAccountById(String accountId) {

        validateRequiredField(accountId, "Account ID");

        for (Account account : accounts) {

            if (account.getAccountId().equals(accountId)) {
                return account;
            }
        }

        return null;
    }

    public void addBranch(Branch branch) {

        if (branch == null) {
            throw new IllegalArgumentException(
                    "Branch cannot be null"
            );
        }

        if (findBranchById(branch.getBranchId()) != null) {
            throw new IllegalArgumentException(
                    "Branch ID already exists"
            );
        }

        branches.add(branch);
    }

    public Branch findBranchById(String branchId) {

        validateRequiredField(branchId, "Branch ID");

        for (Branch branch : branches) {

            if (branch.getBranchId().equals(branchId)) {
                return branch;
            }
        }

        return null;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public String getBankPhone() {
        return bankPhone;
    }

    public List<Customer> getCustomers() {
        return List.copyOf(customers);
    }

    public List<Account> getAccounts() {
        return List.copyOf(accounts);
    }

    public List<Branch> getBranches() {
        return List.copyOf(branches);
    }

    private static void validateRequiredField(
            String value,
            String fieldName) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be null or blank"
            );
        }
    }
}