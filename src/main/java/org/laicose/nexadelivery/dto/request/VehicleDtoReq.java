package org.laicose.nexadelivery.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleDtoReq {

    @NotBlank(message = "type est obligatoire")
    private String type;
    @NotBlank(message = "capacityKg est obligatoire")
    private double capacityKg;
}
