package org.laicose.nexadelivery.mapper;

import org.laicose.nexadelivery.dto.request.ZoneDtoReq;
import org.laicose.nexadelivery.dto.response.ZoneDtoResp;
import org.laicose.nexadelivery.model.Zone;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ZoneMapper {

    Zone toEntityDto(ZoneDtoReq zoneDtoReq);

    ZoneDtoResp toResponse(Zone zone);

    List<ZoneDtoResp> toListDto(List<Zone> zones);
}
