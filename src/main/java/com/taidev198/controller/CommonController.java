package com.taidev198.controller;

import com.taidev198.service.impl.MailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {

    @Autowired
    private MailServiceImpl mailService;

    @PostMapping("/send-email")
    public String sendEmail(
        @RequestParam String to,
        @RequestParam String subject,
        @RequestParam String content,
        @RequestParam(required = false) MultipartFile[] files) {
        try {
            var result = mailService.sendEmail(to, subject, content, files);
            if (result != null) {
                return "index";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "index";
    }
}
