package org.laicose.nexadelivery.deliveryTest;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laicose.nexadelivery.Enum.DeliveryStatus;
import org.laicose.nexadelivery.Enum.DriverStatus;
import org.laicose.nexadelivery.dto.request.DeliveryDtoReq;
import org.laicose.nexadelivery.dto.request.DeliveryStatusReq;
import org.laicose.nexadelivery.dto.response.DeliveryDtoResp;
import org.laicose.nexadelivery.mapper.DeliveryMapper;
import org.laicose.nexadelivery.model.CollectionPoint;
import org.laicose.nexadelivery.model.Delivery;
import org.laicose.nexadelivery.model.Merchant;
import org.laicose.nexadelivery.repository.*;
import org.laicose.nexadelivery.service.DeliveryService;
import org.laicose.nexadelivery.service.NotificationService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private DeliveryMapper deliveryMapper;

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private CollectionPointRepository collectionPointRepository;

    @Mock
    private DriverLocationRepository driverLocationRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private DeliveryService deliveryService;


    @Test
    void createDeliveryTest() {

        String email = "merchant@nexadelivery.com";

        Merchant merchant = new Merchant();

        CollectionPoint collectionPoint = new CollectionPoint();
        collectionPoint.setId(1L);

        DeliveryDtoReq request = new DeliveryDtoReq();
        request.setCollectionPointId(1L);

        Delivery delivery = new Delivery();

        Delivery savedDelivery = new Delivery();
        savedDelivery.setId(10L);
        savedDelivery.setDeliveryStatus(DeliveryStatus.EN_ATTENTE);
        savedDelivery.setCollectionPoint(collectionPoint);

        DeliveryDtoResp response = new DeliveryDtoResp();
        response.setId(10L);
        response.setDeliveryStatus(DeliveryStatus.EN_ATTENTE);
        savedDelivery.setCollectionPoint(collectionPoint);

        when(merchantRepository.findByEmail(email))
                .thenReturn(Optional.of(merchant));

        when(collectionPointRepository.findByIdAndMerchant(1L, merchant))
                .thenReturn(Optional.of(collectionPoint));

        when(deliveryMapper.toEntityDto(request))
                .thenReturn(delivery);

        when(deliveryRepository.save(any(Delivery.class)))
                .thenReturn(savedDelivery);

        when(deliveryRepository.findById(savedDelivery.getId()))
                .thenReturn(Optional.of(savedDelivery));

        when(driverRepository.findByDriverStatus(DriverStatus.DISPONIBLE))
                .thenReturn(List.of());

        when(deliveryMapper.toResponseDto(savedDelivery))
                .thenReturn(response);


        DeliveryDtoResp result =
                deliveryService.createDelivery(email, request);


        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(
                DeliveryStatus.EN_ATTENTE,
                result.getDeliveryStatus()
        );

        verify(merchantRepository).findByEmail(email);
        verify(collectionPointRepository)
                .findByIdAndMerchant(1L, merchant);
        verify(deliveryRepository).save(delivery);
        verify(deliveryMapper).toResponseDto(savedDelivery);
    }


    @Test
    void updateDeliveryStatusTransitionIsInvalidTest() {

        Long deliveryId = 1L;

        Delivery delivery = new Delivery();
        delivery.setDeliveryStatus(DeliveryStatus.EN_ATTENTE);

        DeliveryStatusReq request = new DeliveryStatusReq();
        request.setDeliveryStatus(DeliveryStatus.LIVREE);

        when(deliveryRepository.findById(deliveryId))
                .thenReturn(Optional.of(delivery));


        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> deliveryService.updateDeliveryStatus(
                        deliveryId,
                        request
                )
        );


        assertEquals(
                "Transition de EN_ATTENTE vers LIVREE non autorisée",
                exception.getMessage()
        );

        verify(deliveryRepository).findById(deliveryId);

        verify(deliveryRepository, never())
                .save(any(Delivery.class));

        verifyNoInteractions(notificationService);
    }
}
