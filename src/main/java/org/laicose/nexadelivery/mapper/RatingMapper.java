package org.laicose.nexadelivery.mapper;

import org.laicose.nexadelivery.dto.request.RatingDtoReq;
import org.laicose.nexadelivery.dto.response.RatingDtoResp;
import org.laicose.nexadelivery.model.Rating;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    Rating toEntityDto(RatingDtoReq ratingDtoReq);
    RatingDtoResp toResponseDto(Rating rating);
    List<RatingDtoResp> toListDto(List<Rating> ratings);
}
