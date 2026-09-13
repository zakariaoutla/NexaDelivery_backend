package org.laicose.nexadelivery.NotificationServiceTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laicose.nexadelivery.dto.response.NotificationDtoResp;
import org.laicose.nexadelivery.mapper.NotificationMapper;
import org.laicose.nexadelivery.model.Delivery;
import org.laicose.nexadelivery.model.Notification;
import org.laicose.nexadelivery.model.User;
import org.laicose.nexadelivery.repository.NotificationRepository;
import org.laicose.nexadelivery.repository.UserRepository;
import org.laicose.nexadelivery.service.NotificationService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void createNotificationTest() {

        User user = new User();
        user.setEmail("merchant@nexadelivery.com");

        Delivery delivery = new Delivery();
        delivery.setId(1L);

        String message = "Votre livraison est en route";

        Notification savedNotification = new Notification();
        savedNotification.setId(10L);
        savedNotification.setUser(user);
        savedNotification.setDelivery(delivery);
        savedNotification.setMessage(message);

        NotificationDtoResp response = new NotificationDtoResp();
        response.setId(10L);
        response.setMessage(message);
        response.setDeliveryId(1L);

        when(notificationRepository.save(any(Notification.class)))
                .thenReturn(savedNotification);

        when(notificationMapper.toResponse(savedNotification))
                .thenReturn(response);

        NotificationDtoResp result =
                notificationService.createNotification(
                        user,
                        delivery,
                        message
                );

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(message, result.getMessage());
        assertEquals(1L, result.getDeliveryId());

        verify(notificationRepository)
                .save(any(Notification.class));

        verify(notificationMapper)
                .toResponse(savedNotification);

        verify(messagingTemplate)
                .convertAndSendToUser(
                        "merchant@nexadelivery.com",
                        "/queue/notifications",
                        response
                );
    }

    @Test
    void markAsReadTest() {

        Long notificationId = 1L;

        User owner = new User();
        owner.setEmail("merchant@nexadelivery.com");

        Notification notification = new Notification();
        notification.setId(notificationId);
        notification.setUser(owner);
        notification.setRead(false);

        when(notificationRepository.findById(notificationId))
                .thenReturn(Optional.of(notification));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> notificationService.markAsRead(
                        notificationId,
                        "zakaria@nexadelivery.com"
                )
        );

        assertEquals(
                "Vous n'êtes pas autorisé à modifier cette notification",
                exception.getMessage()
        );

        verify(notificationRepository)
                .findById(notificationId);

        verify(notificationRepository, never())
                .save(any(Notification.class));

        verifyNoInteractions(notificationMapper);
    }
}
