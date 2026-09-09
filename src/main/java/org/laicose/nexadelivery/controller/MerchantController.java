package org.laicose.nexadelivery.controller;


import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.dto.request.MerchantUpdateRequest;
import org.laicose.nexadelivery.dto.response.MerchantDtoResp;
import org.laicose.nexadelivery.service.MerchantService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;

@RestController
@RequestMapping("/api/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;


    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Page<MerchantDtoResp>> findAllMerchant(@PageableDefault(page = 0,size = 10, direction = Sort.Direction.ASC)Pageable pageable){
        return ResponseEntity.ok(merchantService.getAllMerchant(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<MerchantDtoResp> findMerchantById(@PathVariable long id){
        return ResponseEntity.ok(merchantService.getMerchantById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<MerchantDtoResp> updateMerchant(@PathVariable long id, @RequestBody MerchantUpdateRequest merchantUpdateRequest){
        return ResponseEntity.ok(merchantService.updateMerchant(id, merchantUpdateRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<MerchantDtoResp> deletMerchant(@PathVariable long id){
        merchantService.deleteMerchant(id);

        return ResponseEntity.noContent().build();
    }

}
