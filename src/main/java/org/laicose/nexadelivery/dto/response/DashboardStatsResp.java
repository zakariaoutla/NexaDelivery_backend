package org.laicose.nexadelivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsResp {

    private long totalDeliveries;
    private long pendingDeliveries;
    private long assignedDeliveries;
    private long inRouteDeliveries;
    private long deliveredDeliveries;
    private long cancelledDeliveries;

    private long totalDrivers;
    private long availableDrivers;

    private long totalMerchants;
    private long pickedUpDeliveries;
}
