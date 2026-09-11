package com.learning.banking;

import com.learning.banking.model.Depositable;
import com.learning.banking.model.Withdrawable;

public interface TransferService {

    void transfer(
            Withdrawable source,
            Depositable destination,
            double amount
    );
}