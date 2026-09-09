package org.laicose.nexadelivery.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.laicose.nexadelivery.Enum.VehicleType;

@Getter
@Setter
public class VehicleDtoResp {

    private Long id;
    private VehicleType type;
    private Double capacityKg;
}
