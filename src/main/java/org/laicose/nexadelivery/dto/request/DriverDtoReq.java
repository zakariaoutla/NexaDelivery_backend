package org.laicose.nexadelivery.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.laicose.nexadelivery.Enum.DriverStatus;

@Getter
@Setter
public class DriverDtoReq {
    @NotNull(message = "Driver Status est obligatoire")
    private DriverStatus driverStatus;
}
