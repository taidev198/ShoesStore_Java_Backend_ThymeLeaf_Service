package com.taidev198.service.impl;

import com.taidev198.model.Notification;
import com.taidev198.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendNotify(int userId, Notification notification) {
        log.info("send notification to user {}", userId);
        messagingTemplate.convertAndSendToUser(
            String.valueOf(userId),
            "/notification",
            notification
        );
    }
}
