package service.impl;

import model.Account;
import model.Interestable;
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

        account.setBalance(account.getBalance() + amount);
        accountRepo.save(account);
        return true;
    }

    @Override
    public boolean withdraw(String accountNumber, double amount) {
        Account account = accountRepo.findByNumber(accountNumber);
        if (account == null || !account.canWithdraw(amount)) {
            return false;
        }

        account.setBalance(account.getBalance() - amount);
        accountRepo.save(account);
        return true;
    }


    @Override
    public boolean applyInterest(String accountNumber) {
        Account account = accountRepo.findByNumber(accountNumber);

        if (account instanceof Interestable) {
            Interestable interestableAccount = (Interestable) account;
            double interest = interestableAccount.calcInterest();
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
}