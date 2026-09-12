package org.laicose.nexadelivery.mapper;

import org.laicose.nexadelivery.dto.request.DriverLocationDtoReq;
import org.laicose.nexadelivery.dto.response.DriverLocationDtoResp;
import org.laicose.nexadelivery.model.DriverLocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DriverLocationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "driver", ignore = true)
    DriverLocation toEntity(DriverLocationDtoReq request);

    @Mapping(source = "driver.id", target = "driverId")
    DriverLocationDtoResp toResponse(DriverLocation driverLocation);

    List<DriverLocationDtoResp> toListDto(List<DriverLocation> driverLocations);
}
