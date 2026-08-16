package com.booktrack.notification.controller;

import com.booktrack.common.constants.ApplicationConstants;
import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.notification.dto.request.CreateNotificationRequest;
import com.booktrack.notification.dto.response.NotificationResponse;
import com.booktrack.notification.enums.NotificationType;
import com.booktrack.notification.service.NotificationService;
import com.booktrack.security.userdetails.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApplicationConstants.NOTIFICATIONS)
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<NotificationResponse> createNotification(
            @Valid @RequestBody CreateNotificationRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        notificationService.createNotification(
                                request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotificationById(
            @PathVariable Long id,
            Authentication authentication) {

        return ResponseEntity.ok(
                notificationService.getNotificationById(
                        id,
                        authentication));
    }

    @GetMapping("/my")
    public ResponseEntity<PageResponse<NotificationResponse>>
    getMyNotifications(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Long userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                notificationService.getNotificationsByUser(
                        userId,
                        page,
                        size,
                        sortBy,
                        sortDirection));
    }

    @GetMapping("/my/unread")
    public ResponseEntity<PageResponse<NotificationResponse>>
    getMyUnreadNotifications(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Long userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                notificationService.getUnreadNotificationsByUser(
                        userId,
                        page,
                        size,
                        sortBy,
                        sortDirection));
    }

    @GetMapping("/my/type/{type}")
    public ResponseEntity<PageResponse<NotificationResponse>>
    getMyNotificationsByType(
            @PathVariable NotificationType type,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Long userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                notificationService.getNotificationsByUserAndType(
                        userId,
                        type,
                        page,
                        size,
                        sortBy,
                        sortDirection));
    }

    @GetMapping("/my/unread-count")
    public ResponseEntity<Long> getMyUnreadCount(
            Authentication authentication) {

        Long userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                notificationService.getUnreadCount(userId));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(
            @PathVariable Long id,
            Authentication authentication) {

        return ResponseEntity.ok(
                notificationService.markAsRead(
                        id,
                        authentication));
    }

    @PatchMapping("/my/read-all")
    public ResponseEntity<Void> markMyNotificationsAsRead(
            Authentication authentication) {

        Long userId = getAuthenticatedUserId(authentication);

        notificationService.markAllAsRead(userId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<PageResponse<NotificationResponse>>
    getNotificationsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        return ResponseEntity.ok(
                notificationService.getNotificationsByUser(
                        userId,
                        page,
                        size,
                        sortBy,
                        sortDirection));
    }

    @GetMapping("/user/{userId}/unread")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<PageResponse<NotificationResponse>>
    getUnreadNotificationsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        return ResponseEntity.ok(
                notificationService.getUnreadNotificationsByUser(
                        userId,
                        page,
                        size,
                        sortBy,
                        sortDirection));
    }

    @GetMapping("/user/{userId}/type/{type}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<PageResponse<NotificationResponse>>
    getNotificationsByUserAndType(
            @PathVariable Long userId,
            @PathVariable NotificationType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        return ResponseEntity.ok(
                notificationService.getNotificationsByUserAndType(
                        userId,
                        type,
                        page,
                        size,
                        sortBy,
                        sortDirection));
    }

    @GetMapping("/user/{userId}/unread-count")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Long> getUnreadCount(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                notificationService.getUnreadCount(userId));
    }

    private Long getAuthenticatedUserId(
            Authentication authentication) {

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        return userDetails.getUser().getId();
    }
}
