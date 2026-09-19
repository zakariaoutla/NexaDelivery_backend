package org.laicose.nexadelivery.mapper;


import org.laicose.nexadelivery.dto.request.CollectionPointDtoReq;
import org.laicose.nexadelivery.dto.response.CollectionPointDtoResp;
import org.laicose.nexadelivery.model.CollectionPoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CollectionPointMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "merchant", ignore = true)
    CollectionPoint toEntity(CollectionPointDtoReq request);

    @Mapping(source = "merchant.id", target = "merchantId")
    CollectionPointDtoResp toResponse(CollectionPoint collectionPoint);

    List<CollectionPointDtoResp> toListDto(List<CollectionPoint> collectionPoints);
}
