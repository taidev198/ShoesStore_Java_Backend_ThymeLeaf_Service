package com.taidev198.controller;

import com.taidev198.bean.AccountRegistration;
import com.taidev198.bean.ToastMessage;
import com.taidev198.model.Enum.AccountRole;
import com.taidev198.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class RegisterController {

    @Autowired
    private AuthService authService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("accountRegistration", new AccountRegistration());
        return "screens/auth/register";
    }

    @PostMapping("/register")
    public String registerAccount(
        @Valid @ModelAttribute("accountRegistration") AccountRegistration accountRegistration,
        BindingResult bindingResult,
        RedirectAttributes redirectAttrs,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "screens/auth/register";
        }

        try {
            authService.register(accountRegistration, AccountRole.CUSTOMER);
        } catch (Exception ex) {
            model.addAttribute("toastMessages", new ToastMessage("error", ex.getMessage()));
            return "screens/auth/register";
        }

        redirectAttrs.addFlashAttribute(
            "toastMessages",
            new ToastMessage("success",
                "Đăng kí thành công")
        );
        model.addAttribute(
                "linkConfirm",
                "https://mail.google.com/mail/u/0/#inbox"
        );

        return "redirect:/common/confirm-page";
    }

    @GetMapping("/register/confirm/{userId}")
    public String confirmAccount(@PathVariable("userId") int userId, @RequestParam String verifyCode){
        authService.confirm(userId, verifyCode);
        return "redirect:/login";
    }
}