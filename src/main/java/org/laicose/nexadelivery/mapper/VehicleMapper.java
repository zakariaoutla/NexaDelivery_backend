package org.laicose.nexadelivery.mapper;

import org.laicose.nexadelivery.dto.request.VehicleDtoReq;
import org.laicose.nexadelivery.dto.response.VehicleDtoResp;
import org.laicose.nexadelivery.model.Vehicle;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    Vehicle toEntityDto(VehicleDtoReq vehicleDtoReq);

    VehicleDtoResp toResponse(Vehicle vehicle);

    List<VehicleDtoResp> toListDto(List<Vehicle> vehicles);
}
