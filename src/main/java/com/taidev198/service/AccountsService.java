package com.taidev198.service;

import org.springframework.data.domain.Page;

import com.taidev198.model.Account;

public interface AccountsService {
    Page<Account> findAccountsByFilter(int page, int size, String order, String role, String sortBy, String query);

    void toggleAccountActivation(int accountId, boolean active);

    Account findAccountByEmail(String email);
}
