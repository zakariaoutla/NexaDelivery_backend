package org.laicose.nexadelivery.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.laicose.nexadelivery.Enum.DeliveryStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class PublicTrackingDto {

    private String trackingCode;
    private DeliveryStatus deliveryStatus;

    private String pickupAddress;
    private String dropAddress;
    private String description;

    private LocalDateTime createdAt;

    private String merchantName;

    private String driverName;
}