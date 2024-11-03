package com.taidev198.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "chat_room")
@Entity

public class ChatRoom {
    @Id
    @UuidGenerator
    private String id;
    private String chatId;
    private String senderId;
    private String recipientId;
}
