package org.laicose.nexadelivery.service;

import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.Enum.DeliveryStatus;
import org.laicose.nexadelivery.Enum.DriverStatus;
import org.laicose.nexadelivery.dto.response.DashboardStatsResp;
import org.laicose.nexadelivery.repository.DeliveryRepository;
import org.laicose.nexadelivery.repository.DriverRepository;
import org.laicose.nexadelivery.repository.MerchantRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final DeliveryRepository deliveryRepository;
    private final DriverRepository driverRepository;
    private final MerchantRepository merchantRepository;

    public DashboardStatsResp getDashboardStats() {

        DashboardStatsResp stats = new DashboardStatsResp();

        stats.setTotalDeliveries(deliveryRepository.count());

        stats.setPendingDeliveries(
                deliveryRepository.countByDeliveryStatus(DeliveryStatus.EN_ATTENTE)
        );

        stats.setAssignedDeliveries(
                deliveryRepository.countByDeliveryStatus(DeliveryStatus.ASSIGNEE)
        );

        stats.setInRouteDeliveries(
                deliveryRepository.countByDeliveryStatus(DeliveryStatus.EN_ROUTE)
        );

        stats.setDeliveredDeliveries(
                deliveryRepository.countByDeliveryStatus(DeliveryStatus.LIVREE)
        );

        stats.setCancelledDeliveries(
                deliveryRepository.countByDeliveryStatus(DeliveryStatus.ANNULEE)
        );

        stats.setTotalDrivers(driverRepository.count());

        stats.setAvailableDrivers(
                driverRepository.countByDriverStatus(DriverStatus.DISPONIBLE)
        );

        stats.setTotalMerchants(merchantRepository.count());
        stats.setPickedUpDeliveries(
                deliveryRepository.countByDeliveryStatus(DeliveryStatus.RECUPEREE)
        );

        return stats;
    }
}