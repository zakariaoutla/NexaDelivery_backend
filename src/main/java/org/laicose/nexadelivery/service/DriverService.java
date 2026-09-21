package org.laicose.nexadelivery.service;

import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.dto.request.DriverDtoReq;
import org.laicose.nexadelivery.dto.request.DriverUpdateReq;
import org.laicose.nexadelivery.dto.response.DriverDtoResp;
import org.laicose.nexadelivery.mapper.DriverMapper;
import org.laicose.nexadelivery.model.Driver;
import org.laicose.nexadelivery.model.Vehicle;
import org.laicose.nexadelivery.repository.DriverRepository;
import org.laicose.nexadelivery.repository.UserRepository;
import org.laicose.nexadelivery.repository.VehicleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.laicose.nexadelivery.configuration.JwtUtil;
import org.laicose.nexadelivery.dto.response.ProfileUpdateResp;
import org.laicose.nexadelivery.Enum.DriverStatus;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    public Page<DriverDtoResp> getAllDriver(
            String search,
            DriverStatus status,
            Pageable pageable
    ) {

        String normalizedSearch =
                search == null || search.isBlank()
                        ? null
                        : search.trim();

        return driverRepository
                .searchDrivers(
                        normalizedSearch,
                        status,
                        pageable
                )
                .map(driverMapper::toResponseDto);
    }

    public DriverDtoResp getDriverById(Long id){
        Driver driver = driverRepository.findById(id).orElseThrow(()->new RuntimeException("Driver avec l'ID " + id + " est introuvable"));
        return driverMapper.toResponseDto(driver);
    }

    public DriverDtoResp getMyProfile(String email) {

        Driver driver = driverRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Driver avec l'email "
                                        + email
                                        + " est introuvable"
                        )
                );

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

    public ProfileUpdateResp<DriverDtoResp> updateMyProfile(
            String currentEmail,
            DriverUpdateReq request
    ) {

        Driver driver = driverRepository
                .findByEmail(currentEmail)
                .orElseThrow(
                        () -> new RuntimeException("Driver introuvable")
                );

        if (!currentEmail.equalsIgnoreCase(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {

            throw new RuntimeException(
                    "Cette adresse e-mail est déjà utilisée"
            );
        }

        driver.setName(request.getName());
        driver.setEmail(request.getEmail());
        driver.setTelephone(request.getTelephone());

        Driver updatedDriver =
                driverRepository.save(driver);

        String newToken =
                jwtUtil.generateToken(updatedDriver);

        DriverDtoResp driverDto =
                driverMapper.toResponseDto(updatedDriver);

        return new ProfileUpdateResp<>(
                driverDto,
                newToken
        );
    }

    public DriverDtoResp updateDriverStatus(Long id, DriverDtoReq driverDtoReq){
        Driver driver = driverRepository.findById(id).orElseThrow(()->new RuntimeException("Driver avec l'ID " + id + " est introuvable"));

        driver.setDriverStatus(driverDtoReq.getDriverStatus());
        Driver updateDriver = driverRepository.save(driver);
        return driverMapper.toResponseDto(updateDriver);
    }

    public DriverDtoResp updateMyStatus(
            String email,
            DriverDtoReq request
    ) {

        Driver driver = driverRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Driver introuvable")
                );

        DriverStatus currentStatus = driver.getDriverStatus();
        DriverStatus newStatus = request.getDriverStatus();


        if (currentStatus == DriverStatus.EN_ATTENTE_ACCEPTATION) {
            throw new RuntimeException(
                    "Vous devez accepter ou refuser la livraison avant de modifier votre statut"
            );
        }


        if (currentStatus == DriverStatus.EN_LIVRAISON) {
            throw new RuntimeException(
                    "Impossible de modifier votre statut pendant une livraison"
            );
        }


        if (newStatus != DriverStatus.DISPONIBLE
                && newStatus != DriverStatus.HORS_SERVICE) {

            throw new RuntimeException(
                    "Statut non autorisé"
            );
        }


        driver.setDriverStatus(newStatus);

        Driver updatedDriver =
                driverRepository.save(driver);

        return driverMapper.toResponseDto(
                updatedDriver
        );
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


}
