package org.laicose.nexadelivery.controller;

import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.dto.response.PublicTrackingDto;
import org.laicose.nexadelivery.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/tracking")
@RequiredArgsConstructor
public class PublicTrackingController {

    private final DeliveryService deliveryService;

    @GetMapping("/{trackingCode}")
    public ResponseEntity<PublicTrackingDto> trackDelivery(
            @PathVariable String trackingCode
    ) {
        return ResponseEntity.ok(
                deliveryService.trackDelivery(trackingCode)
        );
    }
}