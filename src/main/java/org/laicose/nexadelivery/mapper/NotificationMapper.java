package org.laicose.nexadelivery.mapper;

import org.laicose.nexadelivery.dto.response.NotificationDtoResp;
import org.laicose.nexadelivery.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "delivery.id", target = "deliveryId")
    NotificationDtoResp toResponse(Notification notification);

    List<NotificationDtoResp> toListDto(List<Notification> notifications);
}