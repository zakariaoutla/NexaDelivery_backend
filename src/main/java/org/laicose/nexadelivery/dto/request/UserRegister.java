package org.laicose.nexadelivery.dto.request;


import lombok.Getter;
import lombok.Setter;
import org.laicose.nexadelivery.Enum.Role;

@Getter
@Setter
public class UserRegister {

    private String name;
    private String email;
    private String password;
    private String telephone;
    private Role role;
}
