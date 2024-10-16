package com.taidev198.service.impl;

import com.taidev198.model.Account;
import com.taidev198.model.Enum.AccountRole;
import com.taidev198.repository.AccountRepository;
import com.taidev198.service.AccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AccountsServiceImpl implements AccountsService {

    @Autowired
    private final AccountRepository accountRepository;

    public Page<Account> findAccountsByFilter(int page, int size, String order, String role, String sortBy, String query) {
        Pageable pageable = PageRequest.of(page - 1, size, order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);

        List<AccountRole> roles;
        if ("customer".equalsIgnoreCase(role)) {
            roles = Collections.singletonList(AccountRole.CUSTOMER);
        } else if ("manager".equalsIgnoreCase(role)) {
            roles = Arrays.asList(AccountRole.ADMIN, AccountRole.SELLER);
        } else {
            roles = Arrays.asList(AccountRole.values());
        }

        return accountRepository.findByFilter(query, roles, pageable);
    }

    @Override
    public void toggleAccountActivation(int accountId, boolean active) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account != null) {
            account.setIsActivated(active);
            accountRepository.save(account);
        }
    }

    @Override
    public Account findAccountByEmail(String email) {
        return accountRepository.findByEmail(email).orElse(null);
    }
}
