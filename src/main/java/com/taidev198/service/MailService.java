package com.taidev198.service;

import java.io.UnsupportedEncodingException;

import jakarta.mail.MessagingException;

import org.springframework.web.multipart.MultipartFile;

public interface MailService {
    public String sendEmail(String to, String subject, String content, MultipartFile[] files) throws MessagingException;

    void sendConfirmEmail(String email, Integer id, String secretCode)
            throws MessagingException, UnsupportedEncodingException;
}
