package com.taidev198.service;

import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

public interface MailService {
    public String sendEmail(String to, String subject, String content, MultipartFile[] files)
        throws MessagingException;

    void sendConfirmEmail(String email, Integer id, String secretCode)
        throws MessagingException, UnsupportedEncodingException;
}
