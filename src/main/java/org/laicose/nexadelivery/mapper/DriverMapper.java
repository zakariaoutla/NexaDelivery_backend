package org.laicose.nexadelivery.mapper;


import org.laicose.nexadelivery.dto.request.DriverDtoReq;
import org.laicose.nexadelivery.dto.response.DriverDtoResp;
import org.laicose.nexadelivery.model.Driver;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DriverMapper {

    Driver toEntityDto(DriverDtoReq driverDtoReq);
    DriverDtoResp toResponseDto(Driver driver);

    List<DriverDtoResp> toListDto(List<Driver> drivers);
}
