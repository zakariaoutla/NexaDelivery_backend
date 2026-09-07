package org.laicose.nexadelivery.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.laicose.nexadelivery.Enum.DriverStatus;

@Getter
@Setter
public class DriverDtoReq {

    @NotBlank(message = "Driver Status est obligatoire")
    private DriverStatus driverStatus;
    @NotBlank(message = "Average Rating est obligatoire")
    private double averageRating;
}
