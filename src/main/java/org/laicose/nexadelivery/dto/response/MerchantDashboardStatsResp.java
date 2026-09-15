package org.laicose.nexadelivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MerchantDashboardStatsResp {

    private long totalDeliveries;
    private long pendingDeliveries;
    private long inProgressDeliveries;
    private long deliveredDeliveries;
}