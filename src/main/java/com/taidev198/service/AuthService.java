package com.taidev198.service;

import org.springframework.web.multipart.MultipartFile;

import com.taidev198.bean.*;
import com.taidev198.model.Account;
import com.taidev198.model.Enum.AccountRole;

public interface AuthService {
    Credential login(LoginRequest loginRequest);

    Account register(AccountRegistration accountRegistration, AccountRole role);

    Account updateProfile(ProfileInfo profileInfo);

    Account updatePassword(PasswordInfo passwordInfo);

    Account updateAvatar(MultipartFile image, Account account);

    void confirm(int userId, String verifyCode);
}
