package com.taidev198.model;


import lombok.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ChatNotification {

    private Long id;
    private String message;
    private Integer senderId;
    private Integer receiverId;
}
