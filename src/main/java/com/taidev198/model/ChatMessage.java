package com.taidev198.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@RequiredArgsConstructor
@Table(name = "chat_message")
@AllArgsConstructor
public class ChatMessage extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String message;
    private String chatId;
    private Integer senderId;
    private Integer receiverId;
}
