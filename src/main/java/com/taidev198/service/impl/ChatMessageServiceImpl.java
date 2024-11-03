package com.taidev198.service.impl;

import com.taidev198.model.ChatMessage;
import com.taidev198.repository.ChatMessageRepository;
import com.taidev198.service.ChatMessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatMessageServiceImpl   {

//    private final ChatMessageRepository chatMessageRepository;
//    private final ChatRoomServiceImpl chatRoomService;
//
//    public ChatMessage save(ChatMessage chatMessage) {
//        var chatId =
//            chatRoomService.getChatRoomId(
//                chatMessage.getSenderId(),
//                chatMessage.getReceiverId(),
//                true
//            ).orElseThrow();
//        chatMessage.setChatId(chatId);
//        chatMessageRepository.save(chatMessage);
//        return chatMessage;
//    }
//
//    public List<ChatMessage> findAllChatMessages(Integer senderId, Integer receiverId) {
//        var chatId =
//            chatRoomService.getChatRoomId(senderId, receiverId, false);
//        return chatId.map(chatMessageRepository::findByChatId).orElse(new ArrayList<>());
//    }

}
