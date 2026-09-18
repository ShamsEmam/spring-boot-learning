package view;

import model.Account;
import model.Customer;

public interface StatementPrinter {
    void printCustomerInfo(Customer customer);
    void printAccountInfo(Account account);
}