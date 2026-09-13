package org.laicose.nexadelivery.vehicleTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laicose.nexadelivery.Enum.VehicleType;
import org.laicose.nexadelivery.dto.request.VehicleDtoReq;
import org.laicose.nexadelivery.dto.response.VehicleDtoResp;
import org.laicose.nexadelivery.mapper.VehicleMapper;
import org.laicose.nexadelivery.model.Vehicle;
import org.laicose.nexadelivery.repository.VehicleRepository;
import org.laicose.nexadelivery.service.VehicleService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void createVehicleTest() {

        VehicleDtoReq request = new VehicleDtoReq();
        request.setType(VehicleType.MOTO);
        request.setCapacityKg(50.0);

        Vehicle vehicle = new Vehicle();
        vehicle.setType(VehicleType.MOTO);
        vehicle.setCapacityKg(50.0);

        Vehicle savedVehicle = new Vehicle();
        savedVehicle.setId(1L);
        savedVehicle.setType(VehicleType.MOTO);
        savedVehicle.setCapacityKg(50.0);

        VehicleDtoResp response = new VehicleDtoResp();
        response.setId(1L);
        response.setType(VehicleType.MOTO);
        response.setCapacityKg(50.0);

        when(vehicleMapper.toEntityDto(request)).thenReturn(vehicle);
        when(vehicleRepository.save(vehicle)).thenReturn(savedVehicle);
        when(vehicleMapper.toResponse(savedVehicle)).thenReturn(response);

        VehicleDtoResp result = vehicleService.createVehicle(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(VehicleType.MOTO, result.getType());
        assertEquals(50.0, result.getCapacityKg());

        verify(vehicleRepository).save(vehicle);
        verify(vehicleMapper).toResponse(savedVehicle);
    }

    @Test
    void getVehicleByIdTest() {

        Long vehicleId = 99L;

        when(vehicleRepository.findById(vehicleId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> vehicleService.getVehicleById(vehicleId)
        );

        assertNotNull(exception);

        verify(vehicleRepository).findById(vehicleId);
        verifyNoInteractions(vehicleMapper);
    }
}
