package org.laicose.nexadelivery.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MerchantUpdateRequest {
    private String name;
    private String email;
    private String telephone;
    private String collectionAddress;

    private String currentPassword;
    private String newPassword;
}
