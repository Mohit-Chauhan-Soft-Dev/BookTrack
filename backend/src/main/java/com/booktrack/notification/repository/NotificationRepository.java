package com.booktrack.notification.repository;

import com.booktrack.notification.entity.Notification;
import com.booktrack.notification.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Modifying
    @Query("""
        UPDATE Notification n
        SET n.read = true
        WHERE n.user.id = :userId AND n.read = false
        """)
    int markAllAsRead(@Param("userId") Long userId);
}
