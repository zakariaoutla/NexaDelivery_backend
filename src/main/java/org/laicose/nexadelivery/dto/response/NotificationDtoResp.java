package org.laicose.nexadelivery.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationDtoResp {

    private Long id;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;

    private Long userId;
    private Long deliveryId;
}