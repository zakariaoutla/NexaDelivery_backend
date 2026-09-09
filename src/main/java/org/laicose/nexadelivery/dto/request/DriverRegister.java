package org.laicose.nexadelivery.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverRegister {

    @NotBlank(message = "Le nom est obligatoire")
    private String name;
    @Email
    @NotBlank(message = "email est obligatoire")
    private String email;
    @NotBlank(message = "password est obligatoire")
    private String password;
    @NotBlank(message = "telephone est obligatoire")
    private String telephone;
    @NotNull(message = "Vehicle est obligatoire")
    private Long vehicleId;
    @NotNull(message = "zone est obligatoir")
    private Long zoneId;
}
