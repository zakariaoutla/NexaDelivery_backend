package org.laicose.nexadelivery.mapper;

import org.laicose.nexadelivery.dto.request.DeliveryDtoReq;
import org.laicose.nexadelivery.dto.response.DeliveryDtoResp;
import org.laicose.nexadelivery.model.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

       @Mapping(target = "id", ignore = true)
       @Mapping(target = "deliveryStatus", ignore = true)
       @Mapping(target = "trackingCode", ignore = true)
       @Mapping(target = "createdAt", ignore = true)
       @Mapping(target = "driver", ignore = true)
       @Mapping(target = "merchant", ignore = true)
       @Mapping(target = "rating", ignore = true)
       Delivery toEntityDto(DeliveryDtoReq deliveryDtoReq);

       @Mapping(source = "driver.id", target = "driverId")
       @Mapping(source = "merchant.id", target = "merchantId")
       DeliveryDtoResp toResponseDto(Delivery delivery);

       List<DeliveryDtoResp> toListDto(List<Delivery> deliveries);
}
