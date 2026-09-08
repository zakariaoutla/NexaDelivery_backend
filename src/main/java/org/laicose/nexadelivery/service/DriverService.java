package org.laicose.nexadelivery.service;

import org.laicose.nexadelivery.dto.request.DriverDtoReq;
import org.laicose.nexadelivery.dto.request.DriverRegister;
import org.laicose.nexadelivery.dto.response.DriverDtoResp;
import org.laicose.nexadelivery.mapper.DriverMapper;
import org.laicose.nexadelivery.model.Driver;
import org.laicose.nexadelivery.repository.DriverRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final PasswordEncoder passwordEncoder;

    public DriverService(DriverRepository driverRepository, DriverMapper driverMapper, PasswordEncoder passwordEncoder){
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<DriverDtoResp> getAllDriver(Pageable pageable){
        Page<Driver> drivers = driverRepository.findAll(pageable);
        return drivers.map(driverMapper::toResponseDto);
    }

    public DriverDtoResp getDriverById(long id){
        Driver driver = driverRepository.findById(id).orElseThrow(()->new RuntimeException("Client avec l'ID " + id + " est introuvable"));
        return driverMapper.toResponseDto(driver);
    }

    public DriverDtoResp updateDriver(long id, DriverRegister driverRegister){
        Driver driver = driverRepository.findById(id).orElseThrow(()->new RuntimeException("Client avec l'ID " + id + " est introuvable"));
        driver.setName(driverRegister.getName());
        driver.setEmail(driverRegister.getEmail());
        driver.setTelephone(driverRegister.getTelephone());
        driver.setPassword(passwordEncoder.encode(driverRegister.getPassword()));

        Driver driverUpdate = driverRepository.save(driver);
        return driverMapper.toResponseDto(driverUpdate);
    }

    public DriverDtoResp updateDriverStatus(long id, DriverDtoReq driverDtoReq){
        Driver driver = driverRepository.findById(id).orElseThrow(()->new RuntimeException("Client avec l'ID " + id + " est introuvable"));

        driver.setDriverStatus(driverDtoReq.getDriverStatus());
        Driver updateDriver = driverRepository.save(driver);
        return driverMapper.toResponseDto(updateDriver);
    }

    public void deleteDriver(long id){
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Driver avec l'ID " + id + " est introuvable"
                        )
                );
        driverRepository.delete(driver);
    }


}
