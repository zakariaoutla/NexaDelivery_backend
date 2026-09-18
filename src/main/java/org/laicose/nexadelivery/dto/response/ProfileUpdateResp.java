package org.laicose.nexadelivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProfileUpdateResp<T> {

    private T user;
    private String token;
}