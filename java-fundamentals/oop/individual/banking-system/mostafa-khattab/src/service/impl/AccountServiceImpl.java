package service.impl;

import model.Account;
import repository.AccountRepository;
import service.AccountService;

public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepo;

    public AccountServiceImpl(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Override
    public boolean deposit(String accountNumber, double amount) {
        if (amount <= 0) return false;

        Account account = accountRepo.findByNumber(accountNumber);
        if (account == null) return false;

        double oldBalance = account.getBalance();
        account.setBalance(oldBalance + amount);

        if (accountRepo.save(account)) {
            return true;
        }

        account.setBalance(oldBalance);
        return false;
    }

    @Override
    public boolean withdraw(String accountNumber, double amount) {
        if (amount <= 0) return false;

        Account account = accountRepo.findByNumber(accountNumber);
        if (account == null || !account.canWithdraw(amount)) {
            return false;
        }

        double oldBalance = account.getBalance();
        account.setBalance(oldBalance - amount);

        if (accountRepo.save(account)) {
            return true;
        }

        account.setBalance(oldBalance);
        return false;
    }

    @Override
    public boolean applyInterest(String accountNumber) {
        Account account = accountRepo.findByNumber(accountNumber);
        if (account == null) return false;

        if (account.getAccountType() == Account.AccountType.SAVING) {
            double interest = account.calcInterest();
            if (interest > 0) {
                account.setBalance(account.getBalance() + interest);
                accountRepo.save(account);
                return true;
            }
        }
        return false;
    }

    @Override
    public double getBalance(String accountNumber) {
        Account account = accountRepo.findByNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        return account.getBalance();
    }

    @Override
    public Account getAccount(String accountNumber) {
        return accountRepo.findByNumber(accountNumber);
    }

    @Override
    public boolean transferFunds(
            String fromAccountNumber,
            String toAccountNumber,
            double amount) {

        return accountRepo.transferFunds(
                fromAccountNumber,
                toAccountNumber,
                amount
        );
    }

}