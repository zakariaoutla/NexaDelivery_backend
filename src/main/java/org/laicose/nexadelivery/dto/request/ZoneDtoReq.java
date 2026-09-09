package org.laicose.nexadelivery.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZoneDtoReq {

    @NotBlank(message = "name est obligatoire")
    private String name;
    private String description;
}
