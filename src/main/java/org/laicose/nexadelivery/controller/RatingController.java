package org.laicose.nexadelivery.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.dto.request.RatingDtoReq;
import org.laicose.nexadelivery.dto.response.RatingDtoResp;
import org.laicose.nexadelivery.service.RatingService;
import org.springdoc.core.annotations.ParameterObject;
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
@RequestMapping("/api/rating")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MERCHANT')")
    public ResponseEntity<RatingDtoResp> postRating(@PathVariable Long id, @Valid @RequestBody RatingDtoReq ratingDtoReq, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(ratingService.createRating(id,ratingDtoReq, userDetails.getUsername()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    public ResponseEntity<Page<RatingDtoResp>> getAllRating(@ParameterObject @PageableDefault(page = 0,size = 10,direction = Sort.Direction.ASC)Pageable pageable){
        return ResponseEntity.ok(ratingService.findAllRating(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MERCHANT')")
    public ResponseEntity<RatingDtoResp> getRatingById(@PathVariable Long id){
        return ResponseEntity.ok(ratingService.findRatingById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MERCHANT')")
    public ResponseEntity<RatingDtoResp> putRating(@PathVariable Long id, @Valid @RequestBody RatingDtoReq ratingDtoReq, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(ratingService.updateRating(id,ratingDtoReq, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id){
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build();
    }
}
