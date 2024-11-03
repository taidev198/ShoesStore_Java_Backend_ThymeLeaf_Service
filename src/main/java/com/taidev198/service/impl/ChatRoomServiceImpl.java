package com.taidev198.service.impl;

import com.taidev198.model.ChatRoom;
import com.taidev198.repository.ChatRoomRepository;
import com.taidev198.service.ChatRomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl  {

//    private final ChatRoomRepository chatRoomRepository;
//
//
//    @Override
//    public Optional<String> getChatRoomId(Integer senderId, Integer receiverId, boolean createNewIfNotExisted) {
//        return chatRoomRepository.findBySenderIdAndReceiverId()
//            .map(ChatRoom::getChatId)
//            .or(() ->{
//                if (createNewIfNotExisted) {
//                    var chatId = createChatRoom(senderId, receiverId);
//                    return Optional.of(chatId);
//                }
//                return Optional.empty();
//            });
//    }
//
//    private String createChatRoom(Integer senderId, Integer receiverId) {
//        var chatId = String.format("%s%s", senderId, receiverId);
//
//        ChatRoom senderAndReceiver =
//            ChatRoom.builder()
//                .chatId(chatId)
//                .senderId(senderId)
//                .receiverId(receiverId)
//                .build();
//        ChatRoom receiverAndSender =
//            ChatRoom.builder()
//                .chatId(chatId)
//                .senderId(receiverId)
//                .receiverId(senderId)
//                .build();
//        chatRoomRepository.save(senderAndReceiver);
//        chatRoomRepository.save(receiverAndSender);
//        return chatId;
//    }
}
