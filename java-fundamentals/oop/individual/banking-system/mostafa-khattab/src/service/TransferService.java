package service;

public interface TransferService {
    boolean transfer(String fromAccountNumber, String toAccountNumber, double amount);
}