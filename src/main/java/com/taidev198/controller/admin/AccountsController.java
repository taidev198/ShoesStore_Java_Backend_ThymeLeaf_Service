package com.taidev198.controller.admin;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.taidev198.annotation.CurrentAccount;
import com.taidev198.bean.AccountActivateForm;
import com.taidev198.bean.AccountFilter;
import com.taidev198.bean.AccountRegistration;
import com.taidev198.bean.ToastMessage;
import com.taidev198.model.Account;
import com.taidev198.model.Enum.AccountRole;
import com.taidev198.service.AccountsService;
import com.taidev198.service.AuthService;
import com.taidev198.util.util.PaginationUtil;

import lombok.RequiredArgsConstructor;

@Controller("adminAccountsController")
@RequestMapping("/admin/accounts")
@RequiredArgsConstructor
public class AccountsController {
    private final AccountsService accountService;
    private final AuthService authService;

    @GetMapping
    public String index(@CurrentAccount Account account, @ModelAttribute AccountFilter filter, Model model) {
        Page<Account> accounts = accountService.findAccountsByFilter(
                filter.getPage(), 24, filter.getOrder(), filter.getRole(), filter.getSortBy(), filter.getQuery());

        PaginationUtil paginationHelper = filter.createPaginationUtil((int) accounts.getTotalElements(), 24, 5);

        model.addAttribute("accounts", accounts);
        model.addAttribute("resultCount", accounts.getTotalElements());
        model.addAttribute("paginationHelper", paginationHelper);
        model.addAttribute("totalPages", accounts.getTotalPages());
        model.addAttribute("filter", filter);
        model.addAttribute("currentPage", "account-management");
        return "/screens/admin/accounts/index";
    }

    @PostMapping("/activate")
    public String activateAccount(
            @CurrentAccount Account account, @RequestBody AccountActivateForm accountActivateForm) {
        accountService.toggleAccountActivation(accountActivateForm.getId(), accountActivateForm.isActivate());
        return "redirect:/admin/accounts";
    }

    @GetMapping("/new")
    public String showSellerAccountForm(Model model) {
        model.addAttribute("sellerAccount", new AccountRegistration());
        return "/screens/admin/accounts/new";
    }

    @PostMapping
    public String createSellerAccount(
            @Valid @ModelAttribute("sellerAccount") AccountRegistration sellerAccount,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("sellerAccount", sellerAccount);
            return "/screens/admin/accounts/new";
        }

        try {
            authService.register(sellerAccount, AccountRole.SELLER);
        } catch (Exception ex) {
            model.addAttribute("toastMessages", new ToastMessage("error", ex.getMessage()));
            return "/screens/admin/accounts/new";
        }

        redirectAttrs.addFlashAttribute(
                "toastMessages", new ToastMessage("success", "Tạo tài khoản seller thành công"));
        return "redirect:/admin/accounts";
    }
}
