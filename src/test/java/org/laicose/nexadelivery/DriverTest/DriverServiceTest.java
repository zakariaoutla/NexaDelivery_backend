package org.laicose.nexadelivery.DriverTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laicose.nexadelivery.Enum.DriverStatus;
import org.laicose.nexadelivery.dto.request.DriverDtoReq;
import org.laicose.nexadelivery.dto.response.DriverDtoResp;
import org.laicose.nexadelivery.mapper.DriverMapper;
import org.laicose.nexadelivery.model.Driver;
import org.laicose.nexadelivery.repository.DriverRepository;
import org.laicose.nexadelivery.repository.VehicleRepository;
import org.laicose.nexadelivery.service.DriverService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverMapper driverMapper;



    @InjectMocks
    private DriverService driverService;
    

    @Test
    void getDriverByIdTest() {

        Long driverId = 99L;

        when(driverRepository.findById(driverId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> driverService.getDriverById(driverId)
        );

        assertEquals(
                "Driver avec l'ID 99 est introuvable",
                exception.getMessage()
        );

        verify(driverRepository).findById(driverId);
        verifyNoInteractions(driverMapper);
    }
}
