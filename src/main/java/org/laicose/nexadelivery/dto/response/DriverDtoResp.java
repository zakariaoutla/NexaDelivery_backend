package org.laicose.nexadelivery.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.laicose.nexadelivery.Enum.DriverStatus;
import org.laicose.nexadelivery.Enum.VehicleType;

@Getter
@Setter
public class DriverDtoResp {

    private Long id;
    private String name;
    private String email;
    private String telephone;
    private DriverStatus driverStatus;
    private double averageRating;
    private Long vehicleId;
    private VehicleType vehicleType;


}
