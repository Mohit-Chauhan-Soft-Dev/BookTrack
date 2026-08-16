package com.booktrack.notification.dto.response;

import com.booktrack.notification.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private Long id;

    private Long userId;
    private String userName;
    private String userEmail;

    private NotificationType type;

    private String title;

    private String message;

    private boolean read;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
