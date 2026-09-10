package org.laicose.nexadelivery.mapper;

import org.laicose.nexadelivery.dto.request.RatingDtoReq;
import org.laicose.nexadelivery.dto.response.RatingDtoResp;
import org.laicose.nexadelivery.model.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "delivery", ignore = true)
    Rating toEntityDto(RatingDtoReq ratingDtoReq);

    @Mapping(source = "delivery.id", target = "deliveryId")
    RatingDtoResp toResponseDto(Rating rating);
    List<RatingDtoResp> toListDto(List<Rating> ratings);
}
