package org.laicose.nexadelivery.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.laicose.nexadelivery.Enum.DeliveryStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class DeliveryDtoReq {

    @NotBlank(message = "Pickup Address est obligatoire")
    private String pickupAddress;
    @NotBlank(message = "Drop Address est obligatoire")
    private String dropAddress;
    @NotBlank(message = "description est obligatoire")
    private String description;
    @NotBlank(message = "Delivery Status est obligatoire")
    private DeliveryStatus deliveryStatus;
    @NotBlank(message = "Tarcking Code est obligatoire")
    private String trackingCode;;
    @NotBlank(message = "client Name est obligatoire")
    private String clientName;
    @NotBlank(message = "Client Phone est obligatoire")
    private String clientPhone;
}
