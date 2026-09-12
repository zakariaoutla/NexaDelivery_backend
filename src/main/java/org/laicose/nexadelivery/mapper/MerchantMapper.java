package org.laicose.nexadelivery.mapper;

import org.laicose.nexadelivery.dto.request.MerchantRegister;
import org.laicose.nexadelivery.dto.response.MerchantDtoResp;
import org.laicose.nexadelivery.model.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MerchantMapper {

    @Mapping(target = "deliveries", ignore = true)
    @Mapping(target = "collectionPoints", ignore = true)
    Merchant toEntityDto(MerchantRegister merchantRegister);

    MerchantDtoResp toResponseDto(Merchant merchant);
    List<MerchantDtoResp> toListDto(List<Merchant> merchants);
}
