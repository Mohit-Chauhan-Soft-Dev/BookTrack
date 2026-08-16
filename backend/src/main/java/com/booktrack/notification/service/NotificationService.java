package com.booktrack.notification.service;

import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.notification.dto.request.CreateNotificationRequest;
import com.booktrack.notification.dto.response.NotificationResponse;
import com.booktrack.notification.enums.NotificationType;
import org.springframework.security.core.Authentication;

public interface NotificationService {

    NotificationResponse createNotification(
            CreateNotificationRequest request
    );

    NotificationResponse getNotificationById(
            Long id,
            Authentication authentication
    );

    PageResponse<NotificationResponse> getNotificationsByUser(
            Long userId,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    PageResponse<NotificationResponse> getUnreadNotificationsByUser(
            Long userId,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    PageResponse<NotificationResponse> getNotificationsByUserAndType(
            Long userId,
            NotificationType type,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    NotificationResponse markAsRead(
            Long id,
            Authentication authentication
    );

    void markAllAsRead(
            Long userId
    );

    long getUnreadCount(
            Long userId
    );
}
