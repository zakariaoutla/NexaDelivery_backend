package org.laicose.nexadelivery.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.dto.request.VehicleDtoReq;
import org.laicose.nexadelivery.dto.response.VehicleDtoResp;
import org.laicose.nexadelivery.service.VehicleService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<VehicleDtoResp>> getAllVehicle(@ParameterObject @PageableDefault(page = 0, size = 10, direction = Sort.Direction.ASC)Pageable pageable){
        return ResponseEntity.ok(vehicleService.getAllVehicle(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VehicleDtoResp> getVehicleById(@PathVariable Long id){
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VehicleDtoResp> createVehicle(@Valid @RequestBody VehicleDtoReq vehicleDtoReq){
        return ResponseEntity.ok(vehicleService.createVehicle(vehicleDtoReq));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VehicleDtoResp> updateVehicle(@PathVariable Long id, @Valid @RequestBody VehicleDtoReq vehicleDtoReq){
        return ResponseEntity.ok(vehicleService.updateVehicle(id, vehicleDtoReq));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id){
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }


}

