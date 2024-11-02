package com.taidev198.repository;

import com.taidev198.model.ChatMessage;
import com.taidev198.model.ChatNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Object> {
    List<ChatMessage> findByChatId(String chatId);
}
