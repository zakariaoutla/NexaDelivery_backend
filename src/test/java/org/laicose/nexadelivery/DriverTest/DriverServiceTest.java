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
    void updateDriverStatusTest() {

        Long driverId = 1L;

        DriverDtoReq request = new DriverDtoReq();
        request.setDriverStatus(DriverStatus.DISPONIBLE);

        Driver driver = new Driver();
        driver.setId(driverId);
        driver.setDriverStatus(DriverStatus.HORS_SERVICE);

        Driver updatedDriver = new Driver();
        updatedDriver.setId(driverId);
        updatedDriver.setDriverStatus(DriverStatus.DISPONIBLE);

        DriverDtoResp response = new DriverDtoResp();
        response.setId(driverId);
        response.setDriverStatus(DriverStatus.DISPONIBLE);

        when(driverRepository.findById(driverId))
                .thenReturn(Optional.of(driver));

        when(driverRepository.save(driver))
                .thenReturn(updatedDriver);

        when(driverMapper.toResponseDto(updatedDriver))
                .thenReturn(response);

        DriverDtoResp result =
                driverService.updateDriverStatus(driverId, request);

        assertNotNull(result);
        assertEquals(driverId, result.getId());
        assertEquals(
                DriverStatus.DISPONIBLE,
                result.getDriverStatus()
        );

        verify(driverRepository).findById(driverId);
        verify(driverRepository).save(driver);
        verify(driverMapper).toResponseDto(updatedDriver);
    }

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
