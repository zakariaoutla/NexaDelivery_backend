package org.laicose.nexadelivery.service;

import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.dto.response.NotificationDtoResp;
import org.laicose.nexadelivery.mapper.NotificationMapper;
import org.laicose.nexadelivery.model.Delivery;
import org.laicose.nexadelivery.model.Notification;
import org.laicose.nexadelivery.model.User;
import org.laicose.nexadelivery.repository.NotificationRepository;
import org.laicose.nexadelivery.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public NotificationDtoResp createNotification(
            User user,
            Delivery delivery,
            String message
    ) {

        Notification notification = new Notification();

        notification.setUser(user);
        notification.setDelivery(delivery);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        Notification savedNotification =
                notificationRepository.save(notification);

        NotificationDtoResp response =
                notificationMapper.toResponse(savedNotification);

        messagingTemplate.convertAndSendToUser(
                user.getEmail(),
                "/queue/notifications",
                response
        );
        return response;
    }

    public Page<NotificationDtoResp> getMyNotifications(
            String email,
            Pageable pageable
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur introuvable")
                );

        return notificationRepository
                .findByUserOrderByCreatedAtDesc(user, pageable)
                .map(notificationMapper::toResponse);
    }

    @Transactional
    public NotificationDtoResp markAsRead(
            Long notificationId,
            String email
    ) {

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification introuvable"
                                )
                        );

        if (!notification.getUser()
                .getEmail()
                .equals(email)) {

            throw new RuntimeException(
                    "Vous n'êtes pas autorisé à modifier cette notification"
            );
        }

        notification.setRead(true);

        Notification updatedNotification =
                notificationRepository.save(notification);

        return notificationMapper.toResponse(updatedNotification);
    }

    public long getMyUnreadCount(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur introuvable")
                );

        return notificationRepository
                .countByUserAndReadFalse(user);
    }

}