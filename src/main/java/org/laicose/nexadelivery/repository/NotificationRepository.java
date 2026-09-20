package org.laicose.nexadelivery.repository;

import org.laicose.nexadelivery.model.Notification;
import org.laicose.nexadelivery.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Page<Notification> findByUserOrderByCreatedAtDesc(
            User user,
            Pageable pageable
    );

    long countByUserAndReadFalse(User user);
}
