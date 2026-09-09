package org.laicose.nexadelivery.controller;

import org.laicose.nexadelivery.dto.request.DriverDtoReq;
import org.laicose.nexadelivery.dto.request.DriverRegister;
import org.laicose.nexadelivery.dto.request.DriverUpdateReq;
import org.laicose.nexadelivery.dto.response.DriverDtoResp;
import org.laicose.nexadelivery.service.DriverService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/driver")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService){
        this.driverService = driverService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Page<DriverDtoResp>> findAllDriver(@PageableDefault(page = 0, size = 10, direction = Sort.Direction.ASC)Pageable pageable){
        return ResponseEntity.ok(driverService.getAllDriver(pageable));

    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<DriverDtoResp> findById(@PathVariable long id){
        return ResponseEntity.ok(driverService.getDriverById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<DriverDtoResp> updateDriver(@PathVariable long id, @RequestBody DriverUpdateReq driverUpdateReq){
        return ResponseEntity.ok(driverService.updateDriver(id, driverUpdateReq));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<DriverDtoResp> updateDriverStatus(@PathVariable long id, @RequestBody DriverDtoReq driverDtoReq){
     return ResponseEntity.ok(driverService.updateDriverStatus(id, driverDtoReq));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteDriver(@PathVariable long id){
        driverService.deleteDriver(id);

        return ResponseEntity.noContent().build();
    }

}
