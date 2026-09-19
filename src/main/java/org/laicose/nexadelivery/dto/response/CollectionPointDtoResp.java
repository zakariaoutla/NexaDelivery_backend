package org.laicose.nexadelivery.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectionPointDtoResp {
    private Long id;
    private String address;
    private Double latitude;
    private Double longitude;

    private Long merchantId;
}
