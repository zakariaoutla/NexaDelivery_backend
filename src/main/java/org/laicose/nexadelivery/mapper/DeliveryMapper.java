package org.laicose.nexadelivery.mapper;

import org.laicose.nexadelivery.dto.request.DeliveryDtoReq;
import org.laicose.nexadelivery.dto.response.DeliveryDtoResp;
import org.laicose.nexadelivery.model.Delivery;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
       Delivery toEntityDto(DeliveryDtoReq deliveryDtoReq);
       DeliveryDtoResp toResponseDto(Delivery delivery);

       List<DeliveryDtoResp> toListDto(List<Delivery> deliveries);
}
