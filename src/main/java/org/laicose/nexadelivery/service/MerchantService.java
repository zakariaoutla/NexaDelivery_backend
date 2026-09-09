package org.laicose.nexadelivery.service;

import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.dto.request.MerchantUpdateReq;
import org.laicose.nexadelivery.dto.response.MerchantDtoResp;
import org.laicose.nexadelivery.mapper.MerchantMapper;
import org.laicose.nexadelivery.model.Merchant;
import org.laicose.nexadelivery.repository.MerchantRepository;
import org.laicose.nexadelivery.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;
    private final UserRepository userRepository;

    public Page<MerchantDtoResp> getAllMerchant(Pageable pageable){
        Page<Merchant> merchants = merchantRepository.findAll(pageable);
       return merchants.map(merchantMapper::toResponseDto);
    }

    public MerchantDtoResp getMerchantById(Long id){
        Merchant merchant = merchantRepository.findById(id).orElseThrow(()->new RuntimeException("Merchant avec l'ID " + id + " est introuvable"));
        return merchantMapper.toResponseDto(merchant);
    }

    public MerchantDtoResp getMyProfile(String email) {
        Merchant merchant = merchantRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("Merchant avec l'email " + email + " est introuvable"));
        return merchantMapper.toResponseDto(merchant);
    }

    private MerchantDtoResp applyMerchantUpdate(
            Merchant merchant,
            MerchantUpdateReq request) {

        if (!merchant.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {

            throw new RuntimeException("Cet email est déjà utilisé");
        }

        merchant.setName(request.getName());
        merchant.setEmail(request.getEmail());
        merchant.setTelephone(request.getTelephone());
        merchant.setCollectionAddress(request.getCollectionAddress());

        Merchant updatedMerchant = merchantRepository.save(merchant);

        return merchantMapper.toResponseDto(updatedMerchant);
    }


    public MerchantDtoResp updateMerchant(
            Long id,
            MerchantUpdateReq request) {

        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Merchant avec l'ID " + id + " est introuvable"
                        )
                );
        return applyMerchantUpdate(merchant, request);

    }

    public MerchantDtoResp updateMyProfile(
            String email,
            MerchantUpdateReq request) {

        Merchant merchant = merchantRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Merchant avec l'email " + email + " est introuvable"
                        )
                );

        return applyMerchantUpdate(merchant, request);
    }


    public void deleteMerchant(Long id){
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Merchant avec l'ID " + id + " est introuvable"
                        )
                );
        if(merchant.getDeliveries()!=null && !merchant.getDeliveries().isEmpty()){
            throw new RuntimeException("Impossible de supprimer ce merchant car il possède des livraisons");
        }

        merchantRepository.delete(merchant);
    }

}
