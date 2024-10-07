package com.taidev198.service;

import com.taidev198.bean.Credential;
import com.taidev198.model.Account;

public interface JwtService {
    Account getAccountFromToken(String token);

    Credential generateToken(int accountId);

    Credential refreshToken(String refreshToken);
}
