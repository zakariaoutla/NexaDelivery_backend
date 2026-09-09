package org.laicose.nexadelivery.mapper;


import org.laicose.nexadelivery.dto.request.DriverRegister;
import org.laicose.nexadelivery.dto.response.DriverDtoResp;
import org.laicose.nexadelivery.model.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DriverMapper {

    @Mapping(source = "vehicleId", target = "vehicle.id")
    @Mapping(source = "zoneId", target = "zone.id")
    Driver toEntityDto(DriverRegister driverDtoReq);

    @Mapping(source = "vehicle.id", target = "vehicleId")
    @Mapping(source = "zone.id", target = "zoneId")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "telephone", source = "telephone")
    DriverDtoResp toResponseDto(Driver driver);

    List<DriverDtoResp> toListDto(List<Driver> drivers);
}
