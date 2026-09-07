package org.laicose.nexadelivery.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MerchantDtoReq {
    @NotBlank(message = "Collection Address est obligatoire")
    private String collectionAddress;

}
