package com.learning.banking;

import com.learning.banking.config.ConnectionProvider;
import com.learning.banking.config.DatabaseConnection;
import com.learning.banking.demo.BankingDemo;
import com.learning.banking.repository.AccountRepository;
import com.learning.banking.repository.BranchRepository;
import com.learning.banking.repository.CustomerRepository;
import com.learning.banking.repository.mysql.MySqlAccountRepository;
import com.learning.banking.repository.mysql.MySqlBranchRepository;
import com.learning.banking.repository.mysql.MySqlCustomerRepository;
import com.learning.banking.service.AccountService;
import com.learning.banking.service.BranchService;
import com.learning.banking.service.CustomerService;
import com.learning.banking.service.TransferService;
import com.learning.banking.service.impl.AccountServiceImpl;
import com.learning.banking.service.impl.BranchServiceImpl;
import com.learning.banking.service.impl.CustomerServiceImpl;
import com.learning.banking.service.impl.TransferServiceImpl;

/**
 * COMPOSITION ROOT: the only place that knows which concrete classes are
 * used. It builds the objects and plugs them together (dependency injection).
 *
 *   Main -> services -> repositories -> ConnectionProvider (singleton)
 *
 * To use another database, or the in-memory repositories, only this file
 * changes. No service or model class has to be touched.
 */
public class Main {

    public static void main(String[] args) {

        // Singleton: one shared object that owns the database settings
        ConnectionProvider connectionProvider =
                DatabaseConnection.getInstance();

        // Repositories (data layer)
        CustomerRepository customerRepository =
                new MySqlCustomerRepository(connectionProvider);
        AccountRepository accountRepository =
                new MySqlAccountRepository(connectionProvider);
        BranchRepository branchRepository =
                new MySqlBranchRepository(connectionProvider);

        // Services (business layer) - they only see repository INTERFACES
        CustomerService customerService =
                new CustomerServiceImpl(customerRepository);
        AccountService accountService =
                new AccountServiceImpl(accountRepository, customerRepository);
        BranchService branchService =
                new BranchServiceImpl(branchRepository);
        TransferService transferService =
                new TransferServiceImpl(accountRepository);

        // Presentation: talks to services only, never to repositories
        new BankingDemo(
                customerService,
                accountService,
                branchService,
                transferService
        ).run();
    }
}
