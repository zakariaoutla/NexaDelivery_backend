package org.laicose.nexadelivery.collectionPoint;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laicose.nexadelivery.dto.request.CollectionPointDtoReq;
import org.laicose.nexadelivery.dto.response.CollectionPointDtoResp;
import org.laicose.nexadelivery.mapper.CollectionPointMapper;
import org.laicose.nexadelivery.model.CollectionPoint;
import org.laicose.nexadelivery.model.Merchant;
import org.laicose.nexadelivery.model.Zone;
import org.laicose.nexadelivery.repository.CollectionPointRepository;
import org.laicose.nexadelivery.repository.MerchantRepository;
import org.laicose.nexadelivery.repository.ZoneRepository;
import org.laicose.nexadelivery.service.CollectionPointService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollectionPointServiceTest {

    @Mock
    private CollectionPointMapper collectionPointMapper;

    @Mock
    private CollectionPointRepository collectionPointRepository;

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private ZoneRepository zoneRepository;

    @InjectMocks
    private CollectionPointService collectionPointService;

    @Test
    void createCollectionPointTest() {

        String email = "merchant@nexadelivery.com";

        CollectionPointDtoReq request = new CollectionPointDtoReq();
        request.setAddress("Beni Mellal");
        request.setLatitude(32.3373);
        request.setLongitude(-6.3498);
        request.setZoneId(1L);

        Merchant merchant = new Merchant();

        Zone zone = new Zone();
        zone.setId(1L);

        CollectionPoint collectionPoint = new CollectionPoint();

        CollectionPoint savedCollectionPoint = new CollectionPoint();
        savedCollectionPoint.setId(10L);
        savedCollectionPoint.setAddress("Beni Mellal");
        savedCollectionPoint.setLatitude(32.3373);
        savedCollectionPoint.setLongitude(-6.3498);
        savedCollectionPoint.setMerchant(merchant);
        savedCollectionPoint.setZone(zone);

        CollectionPointDtoResp response = new CollectionPointDtoResp();
        response.setId(10L);
        response.setAddress("Beni Mellal");
        response.setLatitude(32.3373);
        response.setLongitude(-6.3498);
        response.setZoneId(1L);

        when(merchantRepository.findByEmail(email))
                .thenReturn(Optional.of(merchant));

        when(zoneRepository.findById(1L))
                .thenReturn(Optional.of(zone));

        when(collectionPointMapper.toEntity(request))
                .thenReturn(collectionPoint);

        when(collectionPointRepository.save(collectionPoint))
                .thenReturn(savedCollectionPoint);

        when(collectionPointMapper.toResponse(savedCollectionPoint))
                .thenReturn(response);

        CollectionPointDtoResp result =
                collectionPointService.createCollectionPoint(email, request);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Beni Mellal", result.getAddress());
        assertEquals(1L, result.getZoneId());

        verify(merchantRepository).findByEmail(email);
        verify(zoneRepository).findById(1L);
        verify(collectionPointRepository).save(collectionPoint);
        verify(collectionPointMapper).toResponse(savedCollectionPoint);
    }

    @Test
    void getCollectionPointByIdCollectionPointDoesNotExistTest() {

        Long id = 99L;

        when(collectionPointRepository.findById(id))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> collectionPointService.getCollectionPointById(id)
        );

        assertEquals(
                "Collection Point avec l'ID 99 est introuvable",
                exception.getMessage()
        );

        verify(collectionPointRepository).findById(id);
        verifyNoInteractions(collectionPointMapper);
    }
}
