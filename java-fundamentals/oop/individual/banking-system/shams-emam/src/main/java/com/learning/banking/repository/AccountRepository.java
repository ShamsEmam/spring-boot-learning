package com.learning.banking.repository;

import com.learning.banking.model.Account;

import java.util.List;

public interface AccountRepository {

    void save(Account account);

    Account findById(String accountId);

    boolean existsById(String accountId);

    List<Account> findAll();
}