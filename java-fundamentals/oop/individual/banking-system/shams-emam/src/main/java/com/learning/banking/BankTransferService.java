package com.learning.banking;

public class BankTransferService implements TransferService {

    @Override
    public void transfer(
            Withdrawable source,
            Depositable destination,
            double amount) {

        validateSource(source);
        validateDestination(destination);

        if (source == destination) {
            throw new IllegalArgumentException(
                    "Source and destination cannot be the same"
            );
        }

        validateAmount(amount);

        source.withdraw(amount);
        destination.deposit(amount);
    }

    private void validateSource(Withdrawable source) {

        if (source == null) {
            throw new IllegalArgumentException(
                    "Source cannot be null"
            );
        }
    }

    private void validateDestination(
            Depositable destination) {

        if (destination == null) {
            throw new IllegalArgumentException(
                    "Destination cannot be null"
            );
        }
    }

    private void validateAmount(double amount) {

        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException(
                    "Transfer amount must be a valid number greater than zero"
            );
        }
    }
}