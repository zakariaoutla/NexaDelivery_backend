package org.laicose.nexadelivery.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RatingDtoResp {

    private Long id;
    private Integer score;
    private String comment;
    private LocalDateTime createdAt;
    private Long deliveryId;
}
