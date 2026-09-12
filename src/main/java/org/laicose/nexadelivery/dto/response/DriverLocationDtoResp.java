package org.laicose.nexadelivery.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DriverLocationDtoResp {

    private Long id;
    private Double latitude;
    private Double longitude;
    private LocalDateTime timestamp;
    private Long driverId;
}
