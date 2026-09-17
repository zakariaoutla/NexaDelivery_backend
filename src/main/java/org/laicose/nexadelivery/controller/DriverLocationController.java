package org.laicose.nexadelivery.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.dto.request.DriverLocationDtoReq;
import org.laicose.nexadelivery.dto.response.DriverLocationDtoResp;
import org.laicose.nexadelivery.service.DriverLocationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/driver-location")
@RequiredArgsConstructor
public class DriverLocationController {

    private final DriverLocationService driverLocationService;

    @PostMapping
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<DriverLocationDtoResp> createLocation(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody DriverLocationDtoReq request) {

        return ResponseEntity.ok(
                driverLocationService.createLocation(
                        userDetails.getUsername(),
                        request
                )
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<DriverLocationDtoResp>> getAllLocations(
            Pageable pageable) {

        return ResponseEntity.ok(
                driverLocationService.getAllLocations(pageable)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DriverLocationDtoResp> getLocationById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                driverLocationService.getLocationById(id)
        );
    }

    @GetMapping("/delivery/{deliveryId}/latest")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<DriverLocationDtoResp> getLatestLocationByDelivery(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long deliveryId
    ) {

        return ResponseEntity.ok(
                driverLocationService.getLatestLocationByDelivery(
                        userDetails.getUsername(),
                        deliveryId
                )
        );
    }

    @GetMapping("/driver/{driverId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<DriverLocationDtoResp>> getDriverLocations(
            @PathVariable Long driverId,
            Pageable pageable) {

        return ResponseEntity.ok(
                driverLocationService.getDriverLocations(
                        driverId,
                        pageable
                )
        );
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<Page<DriverLocationDtoResp>> getMyLocations(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {

        return ResponseEntity.ok(
                driverLocationService.getMyLocations(
                        userDetails.getUsername(),
                        pageable
                )
        );
    }

    @GetMapping("/driver/{driverId}/latest")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DriverLocationDtoResp> getLatestDriverLocation(
            @PathVariable Long driverId) {

        return ResponseEntity.ok(
                driverLocationService.getLatestDriverLocation(driverId)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLocation(
            @PathVariable Long id) {

        driverLocationService.deleteLocation(id);

        return ResponseEntity.noContent().build();
    }
}
