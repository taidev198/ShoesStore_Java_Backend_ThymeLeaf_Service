package com.taidev198.service.impl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements com.taidev198.service.MailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${api.url}")
    private String apiUrl;

    @Value("${spring.mail.from}")
    String from;

    public String sendEmail(String to, String subject, String content, MultipartFile[] files)
            throws MessagingException {
        log.info("Sending...");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom(from);
        // check to is one or many people
        if (to.contains(",")) {
            mimeMessageHelper.setTo(InternetAddress.parse(to));
        } else {
            mimeMessageHelper.setTo(to);
        }

        // check if files is null or not
        if (files != null) {
            for (MultipartFile file : files) {
                mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
            }
        }
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content, true);
        mailSender.send(mimeMessage);
        log.info("Email sent");
        return "sent";
    }

    @Override
    public void sendConfirmEmail(String email, Integer id, String secretCode)
            throws MessagingException, UnsupportedEncodingException {
        log.info("Sending confirm email...");

        // init mimemessage
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        // put link verify
        Context context = new Context();
        String linkConfirm = String.format("%s/%s?verifyCode=%s", apiUrl, id, secretCode);
        Map<String, Object> property = new HashMap<>();
        property.put("linkConfirm", linkConfirm);
        // context.setVariables(property);
        context.setVariable("linkConfirm", linkConfirm);

        helper.setFrom(from, "Taidev198");
        helper.setTo(email);
        helper.setSubject("Please confirm your account");

        // put context's value to file.html
        String html = templateEngine.process("confirm-email.html", context);
        helper.setText(html, true);

        // send mail
        mailSender.send(mimeMessage);
        log.info("Email sent");
    }
}
