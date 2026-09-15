package org.laicose.nexadelivery.controller;

import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.dto.response.DashboardStatsResp;
import org.laicose.nexadelivery.dto.response.MerchantDashboardStatsResp;
import org.laicose.nexadelivery.service.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardStatsResp> getDashboardStats() {

        return ResponseEntity.ok(
                statisticsService.getDashboardStats()
        );
    }



    @GetMapping("/merchant")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<MerchantDashboardStatsResp> getMerchantDashboardStats(
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        return ResponseEntity.ok(
                statisticsService.getMerchantDashboardStats(
                        userDetails.getUsername()
                )
        );
    }
}