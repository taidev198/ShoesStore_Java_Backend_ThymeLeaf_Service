package com.taidev198.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.taidev198.model.Notification;
import com.taidev198.model.NotificationStatus;
import com.taidev198.service.*;
import jakarta.mail.MessagingException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.taidev198.bean.*;
import com.taidev198.model.Account;
import com.taidev198.model.Enum.AccountRole;
import com.taidev198.model.RefreshToken;
import com.taidev198.repository.AccountRepository;
import com.taidev198.repository.RefreshTokenRepository;
import com.taidev198.util.exception.BadRequestException;
import com.taidev198.util.exception.DuplicateEmailException;
import com.taidev198.util.exception.ForbiddenException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;
    private final MailService mailService;
    private final NotificationService notificationService;

    // Authenticating user and generating new token for one
    @Override
    public Credential login(LoginRequest loginRequest) {
        notificationService.sendNotify(
            4,
            Notification.builder()
                .status(NotificationStatus.RETURN)
                .message("test")
                .title("test")
                .build()
        );
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Account account = (Account) authentication.getPrincipal();
        System.out.println(account.getAuthorities().toString());
        Credential response = jwtService.generateToken(account.getId());
        // Save the refresh token
        refreshTokenRepository.save(RefreshToken.builder()
                .account(account)
                .token(response.getRefreshToken())
                .build());
        return response;
    }

    @Override
    public Account register(AccountRegistration accountRegistration, AccountRole role) {
        if (role == AccountRole.ADMIN) throw new ForbiddenException("Không thể tạo tài khoản admin");

        if (accountRepository.findByEmail(accountRegistration.getEmail()).isPresent())
            throw new DuplicateEmailException("Email đã tồn tại: " + accountRegistration.getEmail());

        String encodedPassword = passwordEncoder.encode(accountRegistration.getPassword());

        Account account = Account.builder()
                .email(accountRegistration.getEmail())
                .fullName(accountRegistration.getFullName())
                .displayName(accountRegistration.getFullName())
                .password(encodedPassword)
                .role(role)
                .address(accountRegistration.getAddress())
                .phoneNumber(accountRegistration.getPhoneNumber())
                .gender(true)
                .isActivated(true)
                .isVerified(0)
                .build();

        try {
            Account newAccount = accountRepository.save(account);
            verifyMailOfUser(newAccount);
            return newAccount;
        } catch (Exception e) {
            throw new BadRequestException("Lưu tài khoản không thành công");
        }
    }

    @Override
    public Account updateProfile(ProfileInfo profileInfo) {
        Account account = accountRepository.findById(profileInfo.getAccountId()).orElse(null);

        if (account == null) {
            return null;
        } else {
            // cap nhap cac thong tin profile ( ngoai tru email)
            account.setFullName(profileInfo.getFullName());
            account.setAddress(profileInfo.getAddress());
            account.setPhoneNumber(profileInfo.getPhoneNumber());
            account.setGender(profileInfo.getGender());
            account.setDisplayName(profileInfo.getDisplayName());
            account.setDateOfBirth(profileInfo.getDateOfBirth());

            try {
                return accountRepository.save(account);
            } catch (Exception e) {
                throw new BadRequestException("Cập nhập tài khoản không thành công");
            }
        }
    }

    @Override
    public Account updatePassword(PasswordInfo passwordInfo) {
        Account account =
                accountRepository.findById(passwordInfo.getAccountId()).orElse(null);
        String encodedPassword = passwordEncoder.encode(passwordInfo.getPassword());
        if (account == null) {
            return null;
        } else {
            // cap nhap mat khau
            account.setPassword(encodedPassword);
            try {
                return accountRepository.save(account);
            } catch (Exception e) {
                throw new BadRequestException("Cập nhập mật khẩu không thành công");
            }
        }
    }

    @Override
    public Account updateAvatar(MultipartFile image, Account account) {

        Map<String, Object> uploadResult = cloudinaryService.uploadFile(image);
        String imageUrl = (String) uploadResult.get("url");
        String publicId = (String) uploadResult.get("public_id");
        account.setAvatarUrl(imageUrl);

        try {
            return accountRepository.save(account);
        } catch (Exception e) {
            throw new BadRequestException("Cập nhập ảnh đại diện không thành công");
        }
    }

    @Override
    public void confirm(int userId, String verifyCode) {
        Account account = accountRepository.findById(userId).orElse(null);
        if (account != null) {
            account.setIsVerified(1);
            accountRepository.save(account);
        }
    }

    private void verifyMailOfUser(Account newAccount) throws MessagingException, UnsupportedEncodingException {
        if (newAccount.getId() != null) {
            mailService.sendConfirmEmail(newAccount.getEmail(), newAccount.getId(), "secretCode");
        }
    }
}
