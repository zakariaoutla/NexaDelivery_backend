package org.laicose.nexadelivery.mapper;

import org.laicose.nexadelivery.dto.request.MerchantRegister;
import org.laicose.nexadelivery.dto.response.MerchantDtoResp;
import org.laicose.nexadelivery.model.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MerchantMapper {

    Merchant toEntityDto(MerchantRegister merchantRegister);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "telephone", source = "telephone")
    MerchantDtoResp toResponseDto(Merchant merchant);
    List<MerchantDtoResp> toListDto(List<Merchant> merchants);
}
