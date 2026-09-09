package org.laicose.nexadelivery.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.laicose.nexadelivery.Enum.DeliveryStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class DeliveryDtoResp {

    private Long id;
    private String pickupAddress;
    private String dropAddress;
    private String description;
    private DeliveryStatus deliveryStatus;
    private String trackingCode;
    private String clientName;
    private String clientPhone;
    private LocalDateTime createdAt;
    private Long driverId;
    private Long merchantId;
}
