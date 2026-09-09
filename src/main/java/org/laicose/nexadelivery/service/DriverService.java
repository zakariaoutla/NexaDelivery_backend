package org.laicose.nexadelivery.service;

import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.dto.request.DriverDtoReq;
import org.laicose.nexadelivery.dto.request.DriverRegister;
import org.laicose.nexadelivery.dto.request.DriverUpdateReq;
import org.laicose.nexadelivery.dto.response.DriverDtoResp;
import org.laicose.nexadelivery.mapper.DriverMapper;
import org.laicose.nexadelivery.model.Driver;
import org.laicose.nexadelivery.model.Vehicle;
import org.laicose.nexadelivery.model.Zone;
import org.laicose.nexadelivery.repository.DriverRepository;
import org.laicose.nexadelivery.repository.VehicleRepository;
import org.laicose.nexadelivery.repository.ZoneRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.SpringDataWebSettings;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final VehicleRepository vehicleRepository;
    private final ZoneRepository zoneRepository;
    private final SpringDataWebSettings springDataWebSettings;


    public Page<DriverDtoResp> getAllDriver(Pageable pageable){
        Page<Driver> drivers = driverRepository.findAll(pageable);
        return drivers.map(driverMapper::toResponseDto);
    }

    public DriverDtoResp getDriverById(Long id){
        Driver driver = driverRepository.findById(id).orElseThrow(()->new RuntimeException("Driver avec l'ID " + id + " est introuvable"));
        return driverMapper.toResponseDto(driver);
    }

    public DriverDtoResp updateDriver(Long id, DriverUpdateReq driverUpdateReq){

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Driver avec l'ID " + id + " est introuvable"
                        )
                );

        driver.setName(driverUpdateReq.getName());
        driver.setEmail(driverUpdateReq.getEmail());
        driver.setTelephone(driverUpdateReq.getTelephone());

        Driver updatedDriver = driverRepository.save(driver);

        return driverMapper.toResponseDto(updatedDriver);
    }

    public DriverDtoResp updateDriverStatus(Long id, DriverDtoReq driverDtoReq){
        Driver driver = driverRepository.findById(id).orElseThrow(()->new RuntimeException("Driver avec l'ID " + id + " est introuvable"));

        driver.setDriverStatus(driverDtoReq.getDriverStatus());
        Driver updateDriver = driverRepository.save(driver);
        return driverMapper.toResponseDto(updateDriver);
    }

    public DriverDtoResp updateMyStatus(
            String email,
            DriverDtoReq request) {

        Driver driver = driverRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Driver introuvable")
                );

        driver.setDriverStatus(request.getDriverStatus());

        Driver updatedDriver = driverRepository.save(driver);

        return driverMapper.toResponseDto(updatedDriver);
    }

    public void deleteDriver(Long id){
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Driver avec l'ID " + id + " est introuvable"
                        )
                );
        driverRepository.delete(driver);
    }

    public DriverDtoResp assignVehicleToDriver(Long driverId, Long vehicleId){
        Driver driver = driverRepository.findById(driverId).orElseThrow(()-> new RuntimeException("Driver avec l'ID " + driverId + " est introuvable"));
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(()-> new RuntimeException("Vehicle avec l'ID " + vehicleId + " est introuvable"));
        if (vehicle.getDriver() != null) {
            throw new RuntimeException(
                    "Ce véhicule est déjà affecté à un driver"
            );
        }
        if (driver.getVehicle() != null) {
            throw new RuntimeException(
                    "Ce driver possède déjà un véhicule"
            );
        }

        driver.setVehicle(vehicle);

        Driver updatedDriver = driverRepository.save(driver);

        return driverMapper.toResponseDto(updatedDriver);

    }

    public DriverDtoResp assignZoneToDriver(Long driverId, Long zoneId){
        Driver driver = driverRepository.findById(driverId).orElseThrow(()-> new RuntimeException("Driver avec l'ID " + driverId + " est introuvable"));
        Zone zone = zoneRepository.findById(zoneId).orElseThrow(()-> new RuntimeException("Zone avec l'ID " + zoneId + " est introuvable"));

        driver.setZone(zone);

        Driver savedDriver = driverRepository.save(driver);
        return driverMapper.toResponseDto(savedDriver);
    }


}
