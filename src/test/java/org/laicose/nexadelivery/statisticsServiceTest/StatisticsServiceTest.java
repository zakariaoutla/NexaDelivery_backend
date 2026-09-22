package org.laicose.nexadelivery.statisticsServiceTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laicose.nexadelivery.Enum.DeliveryStatus;
import org.laicose.nexadelivery.Enum.DriverStatus;
import org.laicose.nexadelivery.dto.response.DashboardStatsResp;
import org.laicose.nexadelivery.repository.DeliveryRepository;
import org.laicose.nexadelivery.repository.DriverRepository;
import org.laicose.nexadelivery.repository.MerchantRepository;
import org.laicose.nexadelivery.service.StatisticsService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private MerchantRepository merchantRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    void getDashboardStatsStatisticsTest() {

        when(deliveryRepository.count()).thenReturn(10L);

        when(deliveryRepository.countByDeliveryStatus(DeliveryStatus.EN_ATTENTE))
                .thenReturn(2L);

        when(deliveryRepository.countByDeliveryStatus(DeliveryStatus.ACCEPTEE))
                .thenReturn(1L);

        when(deliveryRepository.countByDeliveryStatus(DeliveryStatus.ASSIGNEE))
                .thenReturn(1L);

        when(deliveryRepository.countByDeliveryStatus(DeliveryStatus.RECUPEREE))
                .thenReturn(1L);

        when(deliveryRepository.countByDeliveryStatus(DeliveryStatus.EN_ROUTE))
                .thenReturn(2L);

        when(deliveryRepository.countByDeliveryStatus(DeliveryStatus.LIVREE))
                .thenReturn(3L);

        when(deliveryRepository.countByDeliveryStatus(DeliveryStatus.ANNULEE))
                .thenReturn(1L);

        when(driverRepository.count()).thenReturn(5L);

        when(driverRepository.countByDriverStatus(DriverStatus.DISPONIBLE))
                .thenReturn(3L);

        when(merchantRepository.count()).thenReturn(4L);

        DashboardStatsResp result =
                statisticsService.getDashboardStats();

        assertNotNull(result);

        assertEquals(10L, result.getTotalDeliveries());
        assertEquals(2L, result.getPendingDeliveries());
        assertEquals(1L, result.getAssignedDeliveries());
        assertEquals(1L, result.getPickedUpDeliveries());
        assertEquals(2L, result.getInRouteDeliveries());
        assertEquals(3L, result.getDeliveredDeliveries());
        assertEquals(1L, result.getCancelledDeliveries());

        assertEquals(5L, result.getTotalDrivers());
        assertEquals(3L, result.getAvailableDrivers());
        assertEquals(4L, result.getTotalMerchants());

        verify(deliveryRepository).count();
        verify(driverRepository).count();
        verify(merchantRepository).count();
    }

    @Test
    void getDashboardStatsTest() {

        when(deliveryRepository.count()).thenReturn(0L);

        when(deliveryRepository.countByDeliveryStatus(any(DeliveryStatus.class)))
                .thenReturn(0L);

        when(driverRepository.count()).thenReturn(0L);

        when(driverRepository.countByDriverStatus(DriverStatus.DISPONIBLE))
                .thenReturn(0L);

        when(merchantRepository.count()).thenReturn(0L);

        DashboardStatsResp result =
                statisticsService.getDashboardStats();

        assertNotNull(result);

        assertEquals(0L, result.getTotalDeliveries());
        assertEquals(0L, result.getPendingDeliveries());
        assertEquals(0L, result.getAssignedDeliveries());
        assertEquals(0L, result.getPickedUpDeliveries());
        assertEquals(0L, result.getInRouteDeliveries());
        assertEquals(0L, result.getDeliveredDeliveries());
        assertEquals(0L, result.getCancelledDeliveries());

        assertEquals(0L, result.getTotalDrivers());
        assertEquals(0L, result.getAvailableDrivers());
        assertEquals(0L, result.getTotalMerchants());
    }
}
