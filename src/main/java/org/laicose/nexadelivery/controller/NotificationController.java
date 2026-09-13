package org.laicose.nexadelivery.controller;

import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.dto.response.NotificationDtoResp;
import org.laicose.nexadelivery.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT', 'DRIVER')")
    public ResponseEntity<Page<NotificationDtoResp>> getMyNotifications(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                notificationService.getMyNotifications(
                        userDetails.getUsername(),
                        pageable
                )
        );
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT', 'DRIVER')")
    public ResponseEntity<NotificationDtoResp> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        return ResponseEntity.ok(
                notificationService.markAsRead(
                        id,
                        userDetails.getUsername()
                )
        );
    }
}