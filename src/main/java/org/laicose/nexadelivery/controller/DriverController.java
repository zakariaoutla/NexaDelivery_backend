package org.laicose.nexadelivery.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.laicose.nexadelivery.dto.request.DriverDtoReq;
import org.laicose.nexadelivery.dto.request.DriverRegister;
import org.laicose.nexadelivery.dto.request.DriverUpdateReq;
import org.laicose.nexadelivery.dto.response.DriverDtoResp;
import org.laicose.nexadelivery.service.DriverService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/driver")
@SecurityRequirement(name = "bearerAuth")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService){
        this.driverService = driverService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<DriverDtoResp>> findAllDriver(@ParameterObject @PageableDefault(page = 0, size = 10, direction = Sort.Direction.ASC)Pageable pageable){
        return ResponseEntity.ok(driverService.getAllDriver(pageable));

    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DriverDtoResp> findById(@PathVariable long id){
        return ResponseEntity.ok(driverService.getDriverById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DriverDtoResp> updateDriver(@PathVariable long id,@Valid @RequestBody DriverUpdateReq driverUpdateReq){
        return ResponseEntity.ok(driverService.updateDriver(id, driverUpdateReq));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DriverDtoResp> updateDriverStatus(@PathVariable long id,@Valid @RequestBody DriverDtoReq driverDtoReq){
     return ResponseEntity.ok(driverService.updateDriverStatus(id, driverDtoReq));
    }

    @PutMapping("/me/status")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<DriverDtoResp> updateMyStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody DriverDtoReq request) {

        return ResponseEntity.ok(
                driverService.updateMyStatus(
                        userDetails.getUsername(),
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDriver(@PathVariable long id){
        driverService.deleteDriver(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{driverId}/vehicle/{vehicleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DriverDtoResp> assignVehicleToDriver(@PathVariable Long driverId, @PathVariable Long vehicleId ){
        return ResponseEntity.ok(driverService.assignVehicleToDriver(driverId, vehicleId));
    }

    @PutMapping("/{driverId}/zone/{zoneId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DriverDtoResp> assignZoneToDriver(@PathVariable Long driverId, @PathVariable Long zoneId){
        return ResponseEntity.ok(driverService.assignZoneToDriver(driverId, zoneId));
    }

}
