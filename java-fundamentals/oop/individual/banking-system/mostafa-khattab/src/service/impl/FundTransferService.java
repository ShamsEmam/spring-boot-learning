package service.impl;

import service.AccountService;
import service.TransferService;

public class FundTransferService implements TransferService {
    private final AccountService accountService;

    public FundTransferService(AccountService accountService) {
        this.accountService = accountService;
    }


    @Override
    public boolean transfer(
            String fromAccountNumber,
            String toAccountNumber,
            double amount) {

        if (fromAccountNumber == null
                || toAccountNumber == null
                || fromAccountNumber.equals(toAccountNumber)
                || !Double.isFinite(amount)
                || amount <= 0) {
            return false;
        }

        return accountService.transferFunds(
                fromAccountNumber,
                toAccountNumber,
                amount
        );
    }

}