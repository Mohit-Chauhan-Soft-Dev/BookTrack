package com.booktrack.notification.mapper;

import com.booktrack.notification.dto.response.NotificationResponse;
import com.booktrack.notification.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(
            Notification notification) {

        return NotificationResponse.builder()
                .id(notification.getId())

                .userId(
                        notification.getUser().getId())

                .userName(
                        notification.getUser().getFirstName()
                                + " "
                                + notification.getUser().getLastName())

                .userEmail(
                        notification.getUser().getEmail())

                .type(notification.getType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .read(notification.isRead())

                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getUpdatedAt())

                .build();
    }
}
