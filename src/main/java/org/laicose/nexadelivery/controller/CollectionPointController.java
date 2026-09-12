package org.laicose.nexadelivery.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.dto.request.CollectionPointDtoReq;
import org.laicose.nexadelivery.dto.response.CollectionPointDtoResp;
import org.laicose.nexadelivery.service.CollectionPointService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/collection-point")
@RequiredArgsConstructor
public class CollectionPointController {

    private final CollectionPointService collectionPointService;

    @PostMapping
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<CollectionPointDtoResp> createCollectionPoint(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CollectionPointDtoReq request) {

        return ResponseEntity.ok(
                collectionPointService.createCollectionPoint(
                        userDetails.getUsername(),
                        request
                )
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CollectionPointDtoResp>> getAllCollectionPoints(
            Pageable pageable) {

        return ResponseEntity.ok(
                collectionPointService.getAllCollectionPoints(pageable)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CollectionPointDtoResp> getCollectionPointById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                collectionPointService.getCollectionPointById(id)
        );
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<Page<CollectionPointDtoResp>> getMyCollectionPoints(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {

        return ResponseEntity.ok(
                collectionPointService.getMyCollectionPoints(
                        userDetails.getUsername(),
                        pageable
                )
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<CollectionPointDtoResp> updateCollectionPoint(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody CollectionPointDtoReq request) {

        return ResponseEntity.ok(
                collectionPointService.updateCollectionPoint(
                        userDetails.getUsername(),
                        id,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<Void> deleteCollectionPoint(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {

        collectionPointService.deleteCollectionPoint(
                userDetails.getUsername(),
                id
        );

        return ResponseEntity.noContent().build();
    }
}