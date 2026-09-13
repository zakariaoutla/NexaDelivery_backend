package org.laicose.nexadelivery.merchantTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laicose.nexadelivery.dto.request.MerchantUpdateReq;
import org.laicose.nexadelivery.dto.response.MerchantDtoResp;
import org.laicose.nexadelivery.mapper.MerchantMapper;
import org.laicose.nexadelivery.model.Merchant;
import org.laicose.nexadelivery.repository.MerchantRepository;
import org.laicose.nexadelivery.repository.UserRepository;
import org.laicose.nexadelivery.service.MerchantService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MerchantServiceTest {

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private MerchantMapper merchantMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MerchantService merchantService;

    @Test
    void updateMerchantTest() {

        Long merchantId = 1L;

        MerchantUpdateReq request = new MerchantUpdateReq();
        request.setName("Zakaria");
        request.setEmail("merchant@nexadelivery.com");
        request.setTelephone("0607523048");
        request.setBusinessName("WebInHand");

        Merchant merchant = new Merchant();
        merchant.setEmail("zakaria@nexadelivery.com");

        Merchant updatedMerchant = new Merchant();
        updatedMerchant.setName("Zakaria");
        updatedMerchant.setEmail("merchant@nexadelivery.com");
        updatedMerchant.setTelephone("0607523048");
        updatedMerchant.setBusinessName("WebInHand");

        MerchantDtoResp response = new MerchantDtoResp();
        response.setId(merchantId);
        response.setName("Zakaria");
        response.setEmail("merchant@nexadelivery.com");
        response.setTelephone("0607523048");
        response.setBusinessName("WebInHand");

        when(merchantRepository.findById(merchantId))
                .thenReturn(Optional.of(merchant));

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(merchantRepository.save(merchant))
                .thenReturn(updatedMerchant);

        when(merchantMapper.toResponseDto(updatedMerchant))
                .thenReturn(response);

        MerchantDtoResp result =
                merchantService.updateMerchant(merchantId, request);

        assertNotNull(result);
        assertEquals(merchantId, result.getId());
        assertEquals("Zakaria", result.getName());
        assertEquals("merchant@nexadelivery.com", result.getEmail());
        assertEquals("WebInHand", result.getBusinessName());

        verify(merchantRepository).findById(merchantId);
        verify(userRepository).existsByEmail(request.getEmail());
        verify(merchantRepository).save(merchant);
        verify(merchantMapper).toResponseDto(updatedMerchant);
    }

    @Test
    void getMerchantByIdTest() {

        Long merchantId = 99L;

        when(merchantRepository.findById(merchantId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantService.getMerchantById(merchantId)
        );

        assertEquals(
                "Merchant avec l'ID 99 est introuvable",
                exception.getMessage()
        );

        verify(merchantRepository).findById(merchantId);
        verifyNoInteractions(merchantMapper);
    }
}
