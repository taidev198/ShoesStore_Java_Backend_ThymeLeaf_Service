package com.taidev198.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements com.taidev198.service.MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    String from;
    public String sendEmail(String to, String subject, String content, MultipartFile[] files)
        throws MessagingException {
        log.info("Sending...");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper =
            new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom(from);
        //check to is one or many people
        if (to.contains(",")) {
            mimeMessageHelper.setTo(InternetAddress.parse(to));
        } else {
            mimeMessageHelper.setTo(to);
        }

        //check if files is null or not
        if (files != null) {
            for (MultipartFile file : files) {
                mimeMessageHelper
                    .addAttachment(
                    Objects.requireNonNull(file.getOriginalFilename()),
                    file);
            }
        }
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content, true);
        mailSender.send(mimeMessage);
        log.info("Email sent");
        return "sent";
    }
}
