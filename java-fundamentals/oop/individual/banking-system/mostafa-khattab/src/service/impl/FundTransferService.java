package service.impl;

import service.AccountService;
import service.TransferService;

public class FundTransferService implements TransferService {
    private final AccountService accountService;

    public FundTransferService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public boolean transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        if (fromAccountNumber == null || toAccountNumber == null || fromAccountNumber.equals(toAccountNumber)) {
            return false;
        }
        if (amount <= 0) {
            return false;
        }

        boolean isWithdrawn = accountService.withdraw(fromAccountNumber, amount);

        if (isWithdrawn) {
            boolean isDeposited = accountService.deposit(toAccountNumber, amount);

            if (isDeposited) {
                return true;
            } else {
                accountService.deposit(fromAccountNumber, amount);
                return false;
            }
        }

        return false;
    }
}