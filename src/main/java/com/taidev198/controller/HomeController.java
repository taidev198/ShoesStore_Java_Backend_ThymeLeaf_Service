package com.taidev198.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.taidev198.bean.AccountInfo;
import com.taidev198.model.Enum.AccountRole;
import com.taidev198.util.WebUtils;
import com.taidev198.util.constant.CommonConstant;

@Controller
public class HomeController {
    @GetMapping("/")
    public String homepage() {
        var currentAccount = WebUtils.Sessions.getAttribute(CommonConstant.CURRENT_USER, AccountInfo.class);
        if (currentAccount != null && currentAccount.getRole() != AccountRole.CUSTOMER.getIndex()) {
            return "redirect:/admin/statistic";
        }
        return "index";
    }
}
