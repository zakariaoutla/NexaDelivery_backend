package org.laicose.nexadelivery.service;

import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.Enum.DeliveryStatus;
import org.laicose.nexadelivery.Enum.DriverStatus;
import org.laicose.nexadelivery.dto.response.DashboardStatsResp;
import org.laicose.nexadelivery.dto.response.DriverDashboardStatsResp;
import org.laicose.nexadelivery.dto.response.MerchantDashboardStatsResp;
import org.laicose.nexadelivery.model.Driver;
import org.laicose.nexadelivery.model.Merchant;
import org.laicose.nexadelivery.repository.DeliveryRepository;
import org.laicose.nexadelivery.repository.DriverRepository;
import org.laicose.nexadelivery.repository.MerchantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final DeliveryRepository deliveryRepository;
    private final DriverRepository driverRepository;
    private final MerchantRepository merchantRepository;

    

    public DashboardStatsResp getDashboardStats() {

        DashboardStatsResp stats =
                new DashboardStatsResp();


        stats.setTotalDeliveries(
                deliveryRepository.count()
        );


        stats.setPendingDeliveries(
                deliveryRepository.countByDeliveryStatus(
                        DeliveryStatus.EN_ATTENTE
                )
        );


        stats.setAssignedDeliveries(
                deliveryRepository.countByDeliveryStatus(
                        DeliveryStatus.ASSIGNEE
                )
        );

        stats.setAcceptedDeliveries(
                deliveryRepository.countByDeliveryStatus(
                        DeliveryStatus.ACCEPTEE
                )
        );


        stats.setPickedUpDeliveries(
                deliveryRepository.countByDeliveryStatus(
                        DeliveryStatus.RECUPEREE
                )
        );


        stats.setInRouteDeliveries(
                deliveryRepository.countByDeliveryStatus(
                        DeliveryStatus.EN_ROUTE
                )
        );


        stats.setDeliveredDeliveries(
                deliveryRepository.countByDeliveryStatus(
                        DeliveryStatus.LIVREE
                )
        );


        stats.setCancelledDeliveries(
                deliveryRepository.countByDeliveryStatus(
                        DeliveryStatus.ANNULEE
                )
        );

        stats.setActiveDeliveries(
                stats.getAssignedDeliveries()
                        + stats.getAcceptedDeliveries()
                        + stats.getPickedUpDeliveries()
                        + stats.getInRouteDeliveries()
        );


        stats.setTotalDrivers(
                driverRepository.count()
        );


        stats.setAvailableDrivers(
                driverRepository.countByDriverStatus(
                        DriverStatus.DISPONIBLE
                )
        );


        stats.setTotalMerchants(
                merchantRepository.count()
        );


        return stats;
    }



    public MerchantDashboardStatsResp getMerchantDashboardStats(
            String email
    ) {

        Merchant merchant =
                merchantRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Merchant introuvable"
                                        )
                        );


        long totalDeliveries =
                deliveryRepository
                        .countByMerchant(
                                merchant
                        );


        long pendingDeliveries =
                deliveryRepository
                        .countByMerchantAndDeliveryStatus(
                                merchant,
                                DeliveryStatus.EN_ATTENTE
                        );


        long inProgressDeliveries =
                deliveryRepository
                        .countByMerchantAndDeliveryStatusIn(
                                merchant,
                                List.of(
                                        DeliveryStatus.ASSIGNEE,
                                        DeliveryStatus.ACCEPTEE,
                                        DeliveryStatus.RECUPEREE,
                                        DeliveryStatus.EN_ROUTE
                                )
                        );


        long deliveredDeliveries =
                deliveryRepository
                        .countByMerchantAndDeliveryStatus(
                                merchant,
                                DeliveryStatus.LIVREE
                        );


        return new MerchantDashboardStatsResp(
                totalDeliveries,
                pendingDeliveries,
                inProgressDeliveries,
                deliveredDeliveries
        );
    }


    public DriverDashboardStatsResp getDriverDashboardStats(
            String email
    ) {

        Driver driver =
                driverRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Driver introuvable"
                                        )
                        );


        long assignedDeliveries =
                deliveryRepository
                        .countByDriverAndDeliveryStatus(
                                driver,
                                DeliveryStatus.ASSIGNEE
                        );


        long inProgressDeliveries =
                deliveryRepository
                        .countByDriverAndDeliveryStatusIn(
                                driver,
                                List.of(
                                        DeliveryStatus.ACCEPTEE,
                                        DeliveryStatus.RECUPEREE,
                                        DeliveryStatus.EN_ROUTE
                                )
                        );


        long deliveredDeliveries =
                deliveryRepository
                        .countByDriverAndDeliveryStatus(
                                driver,
                                DeliveryStatus.LIVREE
                        );


        return new DriverDashboardStatsResp(
                assignedDeliveries,
                inProgressDeliveries,
                deliveredDeliveries
        );
    }
}