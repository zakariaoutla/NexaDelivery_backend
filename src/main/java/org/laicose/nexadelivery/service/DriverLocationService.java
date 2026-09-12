package org.laicose.nexadelivery.service;

import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.dto.request.DriverLocationDtoReq;
import org.laicose.nexadelivery.dto.response.DriverLocationDtoResp;
import org.laicose.nexadelivery.mapper.DriverLocationMapper;
import org.laicose.nexadelivery.model.Driver;
import org.laicose.nexadelivery.model.DriverLocation;
import org.laicose.nexadelivery.repository.DriverLocationRepository;
import org.laicose.nexadelivery.repository.DriverRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DriverLocationService {

    private final DriverLocationRepository driverLocationRepository;
    private final DriverRepository driverRepository;
    private final DriverLocationMapper driverLocationMapper;

    public DriverLocationDtoResp createLocation(
            String email,
            DriverLocationDtoReq request) {

        Driver driver = driverRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Driver avec l'email " + email + " est introuvable"
                        )
                );

        DriverLocation driverLocation =
                driverLocationMapper.toEntity(request);

        driverLocation.setDriver(driver);
        driverLocation.setTimestamp(LocalDateTime.now());

        DriverLocation savedLocation =
                driverLocationRepository.save(driverLocation);

        return driverLocationMapper.toResponse(savedLocation);
    }

    public Page<DriverLocationDtoResp> getAllLocations(
            Pageable pageable) {

        Page<DriverLocation> locations =
                driverLocationRepository.findAll(pageable);

        return locations.map(driverLocationMapper::toResponse);
    }

    public DriverLocationDtoResp getLocationById(Long id) {

        DriverLocation driverLocation =
                driverLocationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Driver Location avec l'ID "
                                                + id
                                                + " est introuvable"
                                )
                        );

        return driverLocationMapper.toResponse(driverLocation);
    }

    public Page<DriverLocationDtoResp> getDriverLocations(
            Long driverId,
            Pageable pageable) {

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Driver avec l'ID "
                                        + driverId
                                        + " est introuvable"
                        )
                );

        Page<DriverLocation> locations =
                driverLocationRepository
                        .findByDriverOrderByTimestampDesc(
                                driver,
                                pageable
                        );

        return locations.map(driverLocationMapper::toResponse);
    }

    public Page<DriverLocationDtoResp> getMyLocations(
            String email,
            Pageable pageable) {

        Driver driver = driverRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Driver avec l'email "
                                        + email
                                        + " est introuvable"
                        )
                );

        Page<DriverLocation> locations =
                driverLocationRepository
                        .findByDriverOrderByTimestampDesc(
                                driver,
                                pageable
                        );

        return locations.map(driverLocationMapper::toResponse);
    }

    public DriverLocationDtoResp getLatestDriverLocation(Long driverId) {

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Driver avec l'ID "
                                        + driverId
                                        + " est introuvable"
                        )
                );

        DriverLocation location =
                driverLocationRepository
                        .findFirstByDriverOrderByTimestampDesc(driver)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Aucune localisation trouvée pour ce driver"
                                )
                        );

        return driverLocationMapper.toResponse(location);
    }

    public void deleteLocation(Long id) {

        DriverLocation driverLocation =
                driverLocationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Driver Location avec l'ID "
                                                + id
                                                + " est introuvable"
                                )
                        );

        driverLocationRepository.delete(driverLocation);
    }


}
