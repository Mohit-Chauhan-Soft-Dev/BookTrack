package com.booktrack.notification.validator;

import com.booktrack.exception.ResourceNotFoundException;
import com.booktrack.notification.entity.Notification;
import com.booktrack.notification.repository.NotificationRepository;
import com.booktrack.user.entity.User;
import com.booktrack.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationValidator {

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;

    public Notification validateNotificationExists(
            Long id) {

        return notificationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Notification not found with id: "
                                        + id
                        )
                );
    }

    public User validateUserExists(
            Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: "
                                        + userId
                        )
                );
    }
}
