package com.taidev198.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@RequiredArgsConstructor
@Table(name = "chat_room")
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String chatId;
    private Integer senderId;
    private Integer receiverId;
}