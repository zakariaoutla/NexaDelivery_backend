package org.laicose.nexadelivery.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.dto.request.MerchantUpdateReq;
import org.laicose.nexadelivery.dto.response.MerchantDtoResp;
import org.laicose.nexadelivery.service.MerchantService;
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
@RequestMapping("/api/merchant")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MerchantController {

    private final MerchantService merchantService;


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<MerchantDtoResp>> findAllMerchant(@ParameterObject @PageableDefault(page = 0,size = 10, direction = Sort.Direction.ASC)Pageable pageable){
        return ResponseEntity.ok(merchantService.getAllMerchant(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MerchantDtoResp> findMerchantById(@PathVariable Long id){
        return ResponseEntity.ok(merchantService.getMerchantById(id));
    }
    @GetMapping("/me")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<MerchantDtoResp> myProfile(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(merchantService.getMyProfile(userDetails.getUsername()));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<MerchantDtoResp> updateMyProfile(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody MerchantUpdateReq merchantUpdateReq){
        return ResponseEntity.ok(merchantService.updateMyProfile(userDetails.getUsername(), merchantUpdateReq));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MerchantDtoResp> updateMerchant(@PathVariable Long id,@Valid @RequestBody MerchantUpdateReq merchantUpdateReq){
        return ResponseEntity.ok(merchantService.updateMerchant(id, merchantUpdateReq));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMerchant(@PathVariable Long id){
        merchantService.deleteMerchant(id);

        return ResponseEntity.noContent().build();
    }

}
