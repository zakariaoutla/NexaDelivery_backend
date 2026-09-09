package org.laicose.nexadelivery.service;

import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.dto.request.MerchantUpdateRequest;
import org.laicose.nexadelivery.dto.response.MerchantDtoResp;
import org.laicose.nexadelivery.mapper.MerchantMapper;
import org.laicose.nexadelivery.model.Merchant;
import org.laicose.nexadelivery.repository.MerchantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;
    private final PasswordEncoder passwordEncoder;


    public Page<MerchantDtoResp> getAllMerchant(Pageable pageable){
        Page<Merchant> merchants = merchantRepository.findAll(pageable);
       return merchants.map(merchantMapper::toResponseDto);
    }

    public MerchantDtoResp getMerchantById(long id){
        Merchant merchant = merchantRepository.findById(id).orElseThrow(()->new RuntimeException("Merchant avec l'ID " + id + " est introuvable"));
        return merchantMapper.toResponseDto(merchant);
    }

    public MerchantDtoResp updateMerchant(
            long id,
            MerchantUpdateRequest request) {

        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Merchant avec l'ID " + id + " est introuvable"
                        )
                );

        merchant.setName(request.getName());
        merchant.setEmail(request.getEmail());
        merchant.setTelephone(request.getTelephone());
        merchant.setCollectionAddress(request.getCollectionAddress());

        if (request.getNewPassword() != null &&
                !request.getNewPassword().isBlank()) {

            if (!passwordEncoder.matches(
                    request.getCurrentPassword(),
                    merchant.getPassword()
            )) {
                throw new RuntimeException("Ancien mot de passe incorrect");
            }

            merchant.setPassword(
                    passwordEncoder.encode(request.getNewPassword())
            );
        }

        Merchant updatedMerchant = merchantRepository.save(merchant);

        return merchantMapper.toResponseDto(updatedMerchant);
    }

    public void deleteMerchant(long id){
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Merchant avec l'ID " + id + " est introuvable"
                        )
                );

        merchantRepository.delete(merchant);
    }

}
