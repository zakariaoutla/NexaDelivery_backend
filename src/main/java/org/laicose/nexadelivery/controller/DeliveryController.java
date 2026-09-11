package org.laicose.nexadelivery.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.Enum.DeliveryStatus;
import org.laicose.nexadelivery.dto.request.DeliveryDtoReq;
import org.laicose.nexadelivery.dto.request.DeliveryStatusReq;
import org.laicose.nexadelivery.dto.response.DeliveryDtoResp;
import org.laicose.nexadelivery.service.DeliveryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<DeliveryDtoResp>> getAllDelivery(@PageableDefault(page = 0, size = 10, direction = Sort.Direction.ASC)Pageable pageable){
        return ResponseEntity.ok(deliveryService.findAllDelivery(pageable));

    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeliveryDtoResp> getDeliveryById(@PathVariable Long id){
        return ResponseEntity.ok(deliveryService.findDeliveryById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<DeliveryDtoResp> postDelivery(@Valid @RequestBody DeliveryDtoReq deliveryDtoReq, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(deliveryService.createDelivery(userDetails.getUsername(),deliveryDtoReq));

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeliveryDtoResp> putDelivery(@PathVariable Long id, @Valid @RequestBody DeliveryDtoReq deliveryDtoReq){
        return ResponseEntity.ok(deliveryService.updateDelivery(id, deliveryDtoReq));
    }

    @GetMapping("/tracking/{trackingCode}")
    @PreAuthorize("hasAnyRole('ADMIN','MERCHANT','DRIVER')")
    public ResponseEntity<DeliveryDtoResp> findByTrackingCode(@PathVariable String trackingCode){
        return ResponseEntity.ok(deliveryService.findByTrackingCode(trackingCode));
    }

    @PutMapping("/{deliveryId}/driver/{driverId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeliveryDtoResp> assignDriverToDelivery(@PathVariable Long deliveryId, @PathVariable Long driverId){
        return ResponseEntity.ok(deliveryService.assignDriverToDelivery(driverId,deliveryId ));

    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeliveryDtoResp> updateDeliveryStatus(@PathVariable Long id,@Valid @RequestBody DeliveryStatusReq newDelivery){
        return ResponseEntity.ok(deliveryService.updateDeliveryStatus(id, newDelivery));
    }

    @GetMapping("/my-deliveries")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<Page<DeliveryDtoResp>> getMyDeliveries(@AuthenticationPrincipal UserDetails userDetails, @PageableDefault(page = 0,size = 10,direction = Sort.Direction.ASC)Pageable pageable){
        return ResponseEntity.ok(deliveryService.getMyDeliveries(userDetails.getUsername(), pageable));
    }

    @GetMapping("/driver")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<Page<DeliveryDtoResp>> getMyDriverDeliveries(@AuthenticationPrincipal UserDetails userDetails, @PageableDefault(page = 0, size = 10, direction = Sort.Direction.ASC)Pageable pageable){
        return ResponseEntity.ok(deliveryService.getMyDriverDeliveries(userDetails.getUsername(), pageable));
    }

    @PutMapping("/{id}/my-status")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<DeliveryDtoResp> updateMyDeliveryStatus(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id,@Valid @RequestBody DeliveryStatus newStatus){
        return ResponseEntity.ok(deliveryService.updateMyDeliveryStatus(userDetails.getUsername(), id, newStatus));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<DeliveryDtoResp> cancelMyDelivery(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id){
        return ResponseEntity.ok(deliveryService.cancelMyDelivery(userDetails.getUsername(), id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long id){
        deliveryService.deleteDelivery(id);
        return ResponseEntity.noContent().build();
    }



}
