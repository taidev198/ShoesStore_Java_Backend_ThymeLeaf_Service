package com.taidev198.controller;

import com.taidev198.bean.AccountInfo;
import com.taidev198.util.constant.CommonConstant;
import com.taidev198.util.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String homepage() {
        var currentAccount = WebUtils.Sessions.getAttribute(CommonConstant.CURRENT_USER, AccountInfo.class);
        if (currentAccount != null && currentAccount.getRole() != 1) {
            return "redirect:/admin/statistic";
        }
        return "index";
    }
}
