package org.laicose.nexadelivery.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverUpdateReq {

    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    @Email(message = "Format email invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;
}
