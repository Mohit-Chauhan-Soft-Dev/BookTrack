package com.booktrack.notification.repository;

import com.booktrack.notification.entity.Notification;
import com.booktrack.notification.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NotificationRepository extends
        JpaRepository<Notification, Long>,
        JpaSpecificationExecutor<Notification> {

    Page<Notification> findByUserId(
            Long userId,
            Pageable pageable
    );

    Page<Notification> findByUserIdAndReadFalse(
            Long userId,
            Pageable pageable
    );

    long countByUserIdAndReadFalse(
            Long userId
    );

    Page<Notification> findByUserIdAndType(
            Long userId,
            NotificationType type,
            Pageable pageable
    );
}
