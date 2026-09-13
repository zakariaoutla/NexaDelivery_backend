package org.laicose.nexadelivery.driverLocationTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laicose.nexadelivery.dto.request.DriverLocationDtoReq;
import org.laicose.nexadelivery.dto.response.DriverLocationDtoResp;
import org.laicose.nexadelivery.mapper.DriverLocationMapper;
import org.laicose.nexadelivery.model.Driver;
import org.laicose.nexadelivery.model.DriverLocation;
import org.laicose.nexadelivery.repository.DeliveryRepository;
import org.laicose.nexadelivery.repository.DriverLocationRepository;
import org.laicose.nexadelivery.repository.DriverRepository;
import org.laicose.nexadelivery.service.DriverLocationService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverLocationServiceTest {

    @Mock
    private DriverLocationRepository driverLocationRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverLocationMapper driverLocationMapper;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private DriverLocationService driverLocationService;

    @Test
    void createLocationDriverTest() {

        String email = "driver@nexadelivery.com";

        DriverLocationDtoReq request = new DriverLocationDtoReq();
        request.setLatitude(32.3373);
        request.setLongitude(-6.3498);

        Driver driver = new Driver();

        DriverLocation location = new DriverLocation();

        DriverLocation savedLocation = new DriverLocation();
        savedLocation.setId(1L);
        savedLocation.setLatitude(32.3373);
        savedLocation.setLongitude(-6.3498);
        savedLocation.setDriver(driver);

        DriverLocationDtoResp response = new DriverLocationDtoResp();
        response.setId(1L);
        response.setLatitude(32.3373);
        response.setLongitude(-6.3498);

        when(driverRepository.findByEmail(email))
                .thenReturn(Optional.of(driver));

        when(driverLocationMapper.toEntity(request))
                .thenReturn(location);

        when(driverLocationRepository.save(location))
                .thenReturn(savedLocation);

        when(driverLocationMapper.toResponse(savedLocation))
                .thenReturn(response);

        when(deliveryRepository.findFirstByDriverAndDeliveryStatusIn(
                eq(driver),
                anyList()
        )).thenReturn(Optional.empty());

        DriverLocationDtoResp result =
                driverLocationService.createLocation(email, request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(32.3373, result.getLatitude());
        assertEquals(-6.3498, result.getLongitude());

        verify(driverRepository).findByEmail(email);
        verify(driverLocationRepository).save(location);
        verify(driverLocationMapper).toResponse(savedLocation);

        verify(messagingTemplate, never())
                .convertAndSend(anyString(), any(Object.class));
    }

    @Test
    void createLocationDriverDoesNotExist() {

        String email = "zakariaoutla507@nexadelivery.com";

        DriverLocationDtoReq request = new DriverLocationDtoReq();
        request.setLatitude(32.3373);
        request.setLongitude(-6.3498);

        when(driverRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> driverLocationService.createLocation(email, request)
        );

        assertEquals(
                "Driver avec l'email " + email + " est introuvable",
                exception.getMessage()
        );

        verify(driverRepository).findByEmail(email);

        verifyNoInteractions(
                driverLocationRepository,
                driverLocationMapper,
                messagingTemplate,
                deliveryRepository
        );
    }
}
