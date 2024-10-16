package com.taidev198.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.taidev198.annotation.CurrentAccount;
import com.taidev198.bean.PasswordInfo;
import com.taidev198.bean.ProfileInfo;
import com.taidev198.bean.ToastMessage;
import com.taidev198.model.Account;
import com.taidev198.model.Enum.AccountRole;
import com.taidev198.service.AuthService;
import com.taidev198.util.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@RequestMapping("profiles")
@Controller
@RequiredArgsConstructor
public class ProfilesController {
    @Autowired
    private AuthService authService;

    @GetMapping
    public String profiles(Model model, @CurrentAccount Account currentAccount) {
        model.addAttribute(
                "profileInfo",
                ProfileInfo.builder()
                        .accountId(currentAccount.getId())
                        .fullName(currentAccount.getFullName())
                        .dateOfBirth(currentAccount.getDateOfBirth())
                        .phoneNumber(currentAccount.getPhoneNumber())
                        .email(currentAccount.getEmail())
                        .gender(currentAccount.getGender())
                        .address(currentAccount.getAddress())
                        .avatarUrl(currentAccount.getAvatarUrl())
                        .displayName(currentAccount.getDisplayName())
                        .build());
        model.addAttribute("currentAccount", currentAccount);
        model.addAttribute("activeTab", "home");
        model.addAttribute(
                "passwordInfo",
                new PasswordInfo(currentAccount.getId(), currentAccount.getPassword(), currentAccount.getPassword()));
        return redirectRoute(currentAccount);
    }

    @PostMapping("/change-profile")
    public String profiles(
            @Valid @ModelAttribute("profileInfo") ProfileInfo profileInfo,
            @CurrentAccount Account currentAccount,
            RedirectAttributes redirectAttrs,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("currentAccount", currentAccount);
            model.addAttribute("profileInfo", profileInfo);
            model.addAttribute("passwordInfo", new PasswordInfo());
            model.addAttribute("activeTab", "home");
            return redirectRoute(currentAccount);
        }
        try {
            authService.updateProfile(profileInfo);
        } catch (BadRequestException e) {
            model.addAttribute("toastMessages", new ToastMessage("error", e.getMessage()));
            return redirectRoute(currentAccount);
        }
        redirectAttrs.addFlashAttribute("toastMessages", new ToastMessage("success", "Cập nhập thông tin thành công"));
        return "redirect:/profiles";
    }

    @PostMapping("/change-password")
    public String password(
            @Valid @ModelAttribute("passwordInfo") PasswordInfo passwordInfo,
            @CurrentAccount Account currentAccount,
            RedirectAttributes redirectAttrs,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors() || !passwordInfo.isPasswordMatching()) {
            if (!passwordInfo.isPasswordMatching()) {
                bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Mật khẩu xác thực không khớp");
            }
            model.addAttribute("currentAccount", currentAccount);
            model.addAttribute("passwordInfo", new PasswordInfo());
            model.addAttribute(
                    "profileInfo",
                    ProfileInfo.builder()
                            .accountId(currentAccount.getId())
                            .fullName(currentAccount.getFullName())
                            .dateOfBirth(currentAccount.getDateOfBirth())
                            .phoneNumber(currentAccount.getPhoneNumber())
                            .email(currentAccount.getEmail())
                            .gender(currentAccount.getGender())
                            .address(currentAccount.getAddress())
                            .avatarUrl(currentAccount.getAvatarUrl())
                            .displayName(currentAccount.getDisplayName())
                            .build());
            model.addAttribute("activeTab", "password");
            return redirectRoute(currentAccount);
        }
        if (!passwordInfo.getPassword().isEmpty()) {
            try {
                authService.updatePassword(passwordInfo);
            } catch (BadRequestException e) {
                model.addAttribute("toastMessages", new ToastMessage("error", e.getMessage()));
                return redirectRoute(currentAccount);
            }
            redirectAttrs.addFlashAttribute(
                    "toastMessages", new ToastMessage("success", "Cập nhập mật khẩu thành công"));
        }
        return "redirect:/profiles";
    }

    @PostMapping("/change-image")
    public String changeAvatar(
            @CurrentAccount Account currentAccount,
            @RequestParam("imageToAdd") MultipartFile imagesToAdd,
            RedirectAttributes redirectAttrs) {
        try {
            authService.updateAvatar(imagesToAdd, currentAccount);
            redirectAttrs.addFlashAttribute(
                    "toastMessages", new ToastMessage("success", "Cập nhập ảnh đại diện thành công"));
        } catch (BadRequestException e) {
            redirectAttrs.addFlashAttribute("toastMessages", new ToastMessage("error", e.getMessage()));
        }
        return "redirect:/profiles";
    }

    private String redirectRoute(Account currentAccount) {
        if (currentAccount.getRole() == AccountRole.CUSTOMER) {
            return "screens/customer/profiles/show";
        }
        if (currentAccount.getRole() == AccountRole.SELLER) {
            return "screens/seller/profiles/show";
        }
        return "redirect:";
    }
}
