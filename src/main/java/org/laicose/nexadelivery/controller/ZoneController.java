package org.laicose.nexadelivery.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.dto.request.ZoneDtoReq;
import org.laicose.nexadelivery.dto.response.ZoneDtoResp;
import org.laicose.nexadelivery.service.ZoneService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/zone")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ZoneController {

    private final ZoneService zoneService;


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ZoneDtoResp>> findAllZone(@PageableDefault(page = 0, size = 10, direction = Sort.Direction.ASC)Pageable pageable){
        return ResponseEntity.ok(zoneService.getAllZone(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ZoneDtoResp> findZoneById(@PathVariable Long id){
        return ResponseEntity.ok(zoneService.getZoneById(id));
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ZoneDtoResp> createZone(@Valid @RequestBody ZoneDtoReq zoneDtoReq){
        return ResponseEntity.ok(zoneService.createZone(zoneDtoReq));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ZoneDtoResp> updateZone(@PathVariable Long id,@Valid @RequestBody ZoneDtoReq zoneDtoReq){
        return ResponseEntity.ok(zoneService.updateZone(id,zoneDtoReq));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteZone(@PathVariable Long id){
        zoneService.deleteZone(id);
        return ResponseEntity.noContent().build();
    }

}
