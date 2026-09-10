package org.laicose.nexadelivery.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.laicose.nexadelivery.Enum.DeliveryStatus;

@Getter
@Setter
public class DeliveryStatusReq {
    @NotNull
    private DeliveryStatus deliveryStatus;
}
