package org.laicose.nexadelivery.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingDtoReq {

    @NotNull(message = "La note est obligatoire")
    @Min(value = 1, message = "La note doit être au minimum de 1")
    @Max(value = 5, message = "La note doit être au maximum de 5")
    private Integer score;
    @NotBlank(message = "comment est obligatoire")
    private String comment;
}
