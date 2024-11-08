package com.taidev198.controller.chat;

import com.taidev198.model.Account;
import com.taidev198.model.ChatMessage;
import com.taidev198.model.ChatNotification;
import com.taidev198.service.AccountsService;
import com.taidev198.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final AccountsService userService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getChatId(),
                        savedMsg.getSenderId(),
                        savedMsg.getRecipientId(),
                        savedMsg.getContent(),
                        savedMsg.getTimestamp()
                )
        );
    }

    @MessageMapping("/read")
    public void readMessage(@Payload String senderId, @Payload String recipientId) {
        chatMessageService.update(senderId, recipientId);
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,
                                                 @PathVariable String recipientId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

    @GetMapping("/conversions/{id}")
    public ResponseEntity<List<Optional<Account>>> findConversations(@PathVariable String id) {
        return ResponseEntity
                .ok(userService.findAllById(Integer.valueOf(id)));
    }
}
