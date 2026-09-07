package org.laicose.nexadelivery.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLogin {
    @NotBlank(message = "email est obligatoire")
    @Email
    private String email;
    @NotBlank(message = "password obligatoire")
    private String password;
}
