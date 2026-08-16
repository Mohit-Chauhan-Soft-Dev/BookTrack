package com.booktrack.notification.dto.request;

import com.booktrack.notification.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNotificationRequest {

    @NotNull(message = "User is required")
    private Long userId;

    @NotNull(message = "Notification type is required")
    private NotificationType type;

    @NotBlank(message = "Notification title is required")
    @Size(max = 255)
    private String title;

    @NotBlank(message = "Notification message is required")
    @Size(max = 2000)
    private String message;
}
