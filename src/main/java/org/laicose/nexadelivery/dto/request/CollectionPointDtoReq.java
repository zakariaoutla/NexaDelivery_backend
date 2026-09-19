package org.laicose.nexadelivery.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectionPointDtoReq {

    @NotBlank(message = "L'adresse est obligatoire")
    private String address;

    @NotNull(message = "La latitude est obligatoire")
    private Double latitude;

    @NotNull(message = "La longitude est obligatoire")
    private Double longitude;
}
