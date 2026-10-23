package com.learning.banking.model;

import com.learning.banking.util.Validator;

public class Bank {

    private final String bankName;
    private final String bankAddress;
    private final String bankPhone;

    public Bank(
            String bankName,
            String bankAddress,
            String bankPhone) {

        Validator.requireNotBlank(bankName, "Bank name");
        Validator.requireNotBlank(bankAddress, "Bank address");
        Validator.requireNotBlank(bankPhone, "Bank phone");

        this.bankName = bankName;
        this.bankAddress = bankAddress;
        this.bankPhone = bankPhone;
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

}