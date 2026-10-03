package com.learning.banking.model;

public class Bank {

    private final String bankName;
    private final String bankAddress;
    private final String bankPhone;

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