package com.learning.banking.service;

public interface TransferService {

    void transfer(
            String sourceAccountId,
            String destinationAccountId,
            double amount
    );
}