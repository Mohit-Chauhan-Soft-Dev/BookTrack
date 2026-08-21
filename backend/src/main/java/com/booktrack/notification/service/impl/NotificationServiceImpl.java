package com.booktrack.notification.service.impl;

import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.common.mapper.PageResponseMapper;
import com.booktrack.common.util.PageableUtils;
import com.booktrack.exception.ForbiddenException;
import com.booktrack.notification.dto.request.CreateNotificationRequest;
import com.booktrack.notification.dto.response.NotificationResponse;
import com.booktrack.notification.entity.Notification;
import com.booktrack.notification.enums.NotificationType;
import com.booktrack.notification.mapper.NotificationMapper;
import com.booktrack.notification.repository.NotificationRepository;
import com.booktrack.notification.service.NotificationService;
import com.booktrack.notification.validator.NotificationValidator;
import com.booktrack.role.enums.RoleName;
import com.booktrack.security.userdetails.CustomUserDetails;
import com.booktrack.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl
                implements NotificationService {

        private final NotificationRepository notificationRepository;

        private final NotificationMapper notificationMapper;

        private final NotificationValidator notificationValidator;

        private final PageResponseMapper pageResponseMapper;

        @Override
        public NotificationResponse createNotification(
                        CreateNotificationRequest request) {

                User user = notificationValidator.validateUserExists(
                                request.getUserId());

                Notification notification = Notification.builder()
                                .user(user)
                                .type(request.getType())
                                .title(request.getTitle().trim())
                                .message(request.getMessage().trim())
                                .build();

                Notification savedNotification = notificationRepository.save(notification);

                return notificationMapper.toResponse(
                                savedNotification);
        }

        @Override
        @Transactional(readOnly = true)
        public NotificationResponse getNotificationById(
                        Long id,
                        Authentication authentication) {

                Notification notification = notificationValidator
                                .validateNotificationExists(id);

                validateOwnership(
                                notification,
                                authentication);

                return notificationMapper.toResponse(
                                notification);
        }

        @Override
        @Transactional(readOnly = true)
        public PageResponse<NotificationResponse> getNotificationsByUser(
                        Long userId,
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection) {

                notificationValidator.validateUserExists(userId);

                Pageable pageable = PageableUtils.createPageable(
                                page,
                                size,
                                sortBy,
                                sortDirection);

                Page<Notification> notificationPage = notificationRepository.findByUserId(
                                userId,
                                pageable);

                return pageResponseMapper.toPageResponse(
                                notificationPage,
                                notificationMapper::toResponse);
        }

        @Override
        @Transactional(readOnly = true)
        public PageResponse<NotificationResponse> getUnreadNotificationsByUser(
                        Long userId,
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection) {

                notificationValidator.validateUserExists(userId);

                Pageable pageable = PageableUtils.createPageable(
                                page,
                                size,
                                sortBy,
                                sortDirection);

                Page<Notification> notificationPage = notificationRepository.findByUserIdAndReadFalse(
                                userId,
                                pageable);

                return pageResponseMapper.toPageResponse(
                                notificationPage,
                                notificationMapper::toResponse);
        }

        @Override
        @Transactional(readOnly = true)
        public PageResponse<NotificationResponse> getNotificationsByUserAndType(
                        Long userId,
                        NotificationType type,
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection) {

                notificationValidator.validateUserExists(userId);

                Pageable pageable = PageableUtils.createPageable(
                                page,
                                size,
                                sortBy,
                                sortDirection);

                Page<Notification> notificationPage = notificationRepository.findByUserIdAndType(
                                userId,
                                type,
                                pageable);

                return pageResponseMapper.toPageResponse(
                                notificationPage,
                                notificationMapper::toResponse);
        }

        @Override
        public NotificationResponse markAsRead(
                        Long id,
                        Authentication authentication) {

                Notification notification = notificationValidator
                                .validateNotificationExists(id);

                validateOwnership(
                                notification,
                                authentication);

                notification.setRead(true);

                Notification updatedNotification = notificationRepository.save(notification);

                return notificationMapper.toResponse(
                                updatedNotification);
        }

        @Override
        public void markAllAsRead(
                        Long userId) {

                notificationValidator.validateUserExists(userId);

                notificationRepository.markAllAsRead(userId);
        }

        @Override
        @Transactional(readOnly = true)
        public long getUnreadCount(
                        Long userId) {

                notificationValidator.validateUserExists(userId);

                return notificationRepository
                                .countByUserIdAndReadFalse(userId);
        }

        private void validateOwnership(
                        Notification notification,
                        Authentication authentication) {

                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

                User authenticatedUser = userDetails.getUser();

                boolean isAdmin = authenticatedUser.getRoles()
                                .stream()
                                .anyMatch(role -> role.getName() == RoleName.ROLE_ADMIN
                                                || role.getName() == RoleName.ROLE_SUPER_ADMIN);

                boolean isOwner = notification.getUser().getId()
                                .equals(authenticatedUser.getId());

                if (!isOwner && !isAdmin) {

                        throw new ForbiddenException(
                                        "You are not authorized to access this notification.");
                }
        }

}
