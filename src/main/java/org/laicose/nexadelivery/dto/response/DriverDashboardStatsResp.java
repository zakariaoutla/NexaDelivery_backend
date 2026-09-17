package org.laicose.nexadelivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DriverDashboardStatsResp {

    private long assignedDeliveries;
    private long inProgressDeliveries;
    private long deliveredDeliveries;
}