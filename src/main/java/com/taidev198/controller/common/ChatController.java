package com.taidev198.controller.common;

import com.taidev198.model.ChatMessage;
import com.taidev198.model.ChatNotification;
import com.taidev198.service.impl.ChatMessageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageServiceImpl chatMessageService;

    @MessageMapping("chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
            String.valueOf(chatMessage.getReceiverId()),
            "/queue/messages",
            ChatNotification.builder()
                .id(chatMessage.getId())
                .message(chatMessage.getMessage())
                .senderId(chatMessage.getSenderId())
                .receiverId(chatMessage.getReceiverId())
                .build()
        );
    }

    @GetMapping("/messages/{senderId}/{receiverId}")
    public String chatRoom(
        @PathVariable("senderId") Integer senderId,
        @PathVariable("receiverId") Integer receiverId
    ) {
       List<ChatMessage> messages = chatMessageService.findAllChatMessages(senderId, receiverId);
        return "screens/chat/index";
    }
}