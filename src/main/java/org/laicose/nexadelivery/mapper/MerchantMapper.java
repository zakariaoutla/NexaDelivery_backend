package org.laicose.nexadelivery.mapper;

import org.laicose.nexadelivery.dto.request.MerchantRegister;
import org.laicose.nexadelivery.dto.response.MerchantDtoResp;
import org.laicose.nexadelivery.model.Merchant;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MerchantMapper {

    Merchant toEntityDto(MerchantRegister merchantRegister);
    MerchantDtoResp toResponseDto(Merchant merchant);
    List<MerchantDtoResp> toListDto(List<Merchant> merchants);
}
