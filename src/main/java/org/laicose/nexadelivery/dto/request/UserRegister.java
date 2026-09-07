package org.laicose.nexadelivery.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.laicose.nexadelivery.Enum.Role;

@Getter
@Setter
public class UserRegister {

    @NotBlank(message = "Le nom est obligatoire")
    private String name;
    @Email
    @NotBlank(message = "email est obligatoire")
    private String email;
    @NotBlank(message = "password est obligatoire")
    private String password;
    @NotBlank(message = "telephone est obligatoire")
    private String telephone;
    @NotBlank(message = "role est obligatoire")
    private Role role;
}
