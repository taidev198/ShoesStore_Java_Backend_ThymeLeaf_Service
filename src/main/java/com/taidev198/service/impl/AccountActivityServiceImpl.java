package com.taidev198.service.impl;

import com.taidev198.model.AccountActivity;
import com.taidev198.repository.AccountActivityRepository;
import com.taidev198.repository.AccountRepository;
import com.taidev198.service.AccountActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountActivityServiceImpl implements AccountActivityService {

    @Autowired
    private AccountActivityRepository accountActivityRepository;


    public AccountActivity save(AccountActivity accountActivity) {

        return accountActivityRepository.save(accountActivity);
    }
}
