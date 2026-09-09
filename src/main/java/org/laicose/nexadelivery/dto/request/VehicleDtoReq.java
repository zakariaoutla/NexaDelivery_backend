package org.laicose.nexadelivery.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.laicose.nexadelivery.Enum.VehicleType;

@Getter
@Setter
public class VehicleDtoReq {

    @NotNull(message = "type est obligatoire")
    private VehicleType type;
    @NotNull(message = "capacityKg est obligatoire")
    @DecimalMin(value = "0.1", message = "La capacité doit être supérieure à 0")
    private Double capacityKg;
}
