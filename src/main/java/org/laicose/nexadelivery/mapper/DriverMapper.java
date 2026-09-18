package org.laicose.nexadelivery.mapper;


import org.laicose.nexadelivery.dto.request.DriverRegister;
import org.laicose.nexadelivery.dto.response.DriverDtoResp;
import org.laicose.nexadelivery.model.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DriverMapper {

    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "zone", ignore = true)
    @Mapping(target = "deliveries", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    Driver toEntityDto(DriverRegister driverRegister);

    @Mapping(source = "vehicle.id", target = "vehicleId")
    @Mapping(source = "vehicle.type", target = "vehicleType")
    @Mapping(source = "zone.id", target = "zoneId")
    @Mapping(source = "zone.name", target = "zoneName")
    DriverDtoResp toResponseDto(Driver driver);

    List<DriverDtoResp> toListDto(List<Driver> drivers);
}
