package com.taidev198.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    private NotificationStatus status;
    private String message;
    private String title;

}
