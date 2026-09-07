package com.learning.banking;

public interface TransferService {

    void transfer(
            Withdrawable source,
            Depositable destination,
            double amount
    );
}