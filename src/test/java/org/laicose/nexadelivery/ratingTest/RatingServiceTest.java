package org.laicose.nexadelivery.ratingTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laicose.nexadelivery.Enum.DeliveryStatus;
import org.laicose.nexadelivery.dto.request.RatingDtoReq;
import org.laicose.nexadelivery.dto.response.RatingDtoResp;
import org.laicose.nexadelivery.mapper.RatingMapper;
import org.laicose.nexadelivery.model.Delivery;
import org.laicose.nexadelivery.model.Driver;
import org.laicose.nexadelivery.model.Merchant;
import org.laicose.nexadelivery.model.Rating;
import org.laicose.nexadelivery.repository.DeliveryRepository;
import org.laicose.nexadelivery.repository.DriverRepository;
import org.laicose.nexadelivery.repository.MerchantRepository;
import org.laicose.nexadelivery.repository.RatingRepository;
import org.laicose.nexadelivery.service.RatingService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RatingMapper ratingMapper;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private MerchantRepository merchantRepository;

    @InjectMocks
    private RatingService ratingService;

    @Test
    void createRatingTest() {

        Long deliveryId = 1L;
        String email = "merchant@nexadelivery.com";

        Merchant merchant = new Merchant();
        merchant.setId(10L);

        Driver driver = new Driver();
        driver.setId(20L);

        Delivery delivery = new Delivery();
        delivery.setId(deliveryId);
        delivery.setDeliveryStatus(DeliveryStatus.LIVREE);
        delivery.setMerchant(merchant);
        delivery.setDriver(driver);

        RatingDtoReq request = new RatingDtoReq();
        request.setScore(5);
        request.setComment("Excellent");

        Rating rating = new Rating();

        Rating savedRating = new Rating();
        savedRating.setId(100L);
        savedRating.setScore(5);
        savedRating.setComment("Excellent");
        savedRating.setDelivery(delivery);

        RatingDtoResp response = new RatingDtoResp();
        response.setId(100L);
        response.setScore(5);
        response.setComment("Excellent");

        when(deliveryRepository.findById(deliveryId))
                .thenReturn(Optional.of(delivery));

        when(ratingRepository.existsByDeliveryId(deliveryId))
                .thenReturn(false);

        when(merchantRepository.findByEmail(email))
                .thenReturn(Optional.of(merchant));

        when(ratingMapper.toEntityDto(request))
                .thenReturn(rating);

        when(ratingRepository.save(rating))
                .thenReturn(savedRating);

        when(ratingRepository.findAverageByDriverId(driver.getId()))
                .thenReturn(5.0);

        when(driverRepository.save(driver))
                .thenReturn(driver);

        when(ratingMapper.toResponseDto(savedRating))
                .thenReturn(response);

        RatingDtoResp result =
                ratingService.createRating(deliveryId, request, email);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(5, result.getScore());
        assertEquals("Excellent", result.getComment());
        assertEquals(5.0, driver.getAverageRating());

        verify(ratingRepository).save(rating);
        verify(driverRepository).save(driver);
        verify(ratingMapper).toResponseDto(savedRating);
    }

    @Test
    void createRatingDeliveryIsNotDeliveredTest() {

        Long deliveryId = 1L;

        Delivery delivery = new Delivery();
        delivery.setDeliveryStatus(DeliveryStatus.EN_ROUTE);

        RatingDtoReq request = new RatingDtoReq();
        request.setScore(4);
        request.setComment("Bien");

        when(deliveryRepository.findById(deliveryId))
                .thenReturn(Optional.of(delivery));

        when(ratingRepository.existsByDeliveryId(deliveryId))
                .thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> ratingService.createRating(
                        deliveryId,
                        request,
                        "merchant@nexadelivery.com"
                )
        );

        assertEquals(
                "Vous ne pouvez évaluer qu'une livraison livrée",
                exception.getMessage()
        );

        verify(deliveryRepository).findById(deliveryId);
        verify(ratingRepository).existsByDeliveryId(deliveryId);

        verify(ratingRepository, never()).save(any(Rating.class));
        verifyNoInteractions(ratingMapper);
    }
}
