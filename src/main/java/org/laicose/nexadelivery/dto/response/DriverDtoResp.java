package org.laicose.nexadelivery.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.laicose.nexadelivery.Enum.DriverStatus;

@Getter
@Setter
public class DriverDtoResp {

    private Long id;
    private String name;
    private String email;
    private String telephone;
    private DriverStatus driverStatus;
    private double averageRating;
    private long vehicleId;
    private long zoneId;

}
