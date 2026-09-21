package org.laicose.nexadelivery.repository;

import org.laicose.nexadelivery.Enum.DeliveryStatus;
import org.laicose.nexadelivery.model.Delivery;
import org.laicose.nexadelivery.model.Driver;
import org.laicose.nexadelivery.model.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findByTrackingCode(String trackingCode);

    Page<Delivery> findByMerchant(Merchant merchant, Pageable pageable);
    Page<Delivery> findByDriver(Driver driver, Pageable pageable);

    Optional<Delivery> findFirstByDriverAndDeliveryStatusIn(
            Driver driver,
            List<DeliveryStatus> statuses
    );

    long countByDeliveryStatus(DeliveryStatus deliveryStatus);

    long countByMerchant(
            Merchant merchant
    );

    long countByMerchantAndDeliveryStatus(
            Merchant merchant,
            DeliveryStatus deliveryStatus
    );

    long countByMerchantAndDeliveryStatusIn(
            Merchant merchant,
            List<DeliveryStatus> statuses
    );

    Optional<Delivery> findByIdAndMerchant(
            Long id,
            Merchant merchant
    );

    long countByDriverAndDeliveryStatus(
            Driver driver,
            DeliveryStatus deliveryStatus
    );

    long countByDriverAndDeliveryStatusIn(
            Driver driver,
            List<DeliveryStatus> statuses
    );

    List<Delivery> findByDeliveryStatus(
            DeliveryStatus deliveryStatus
    );

    @Query("""
    SELECT d
    FROM Delivery d
    LEFT JOIN d.driver dr
    LEFT JOIN d.merchant m
    WHERE
        (
            :search IS NULL
            OR LOWER(d.trackingCode) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(d.clientName) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(d.dropAddress) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(dr.name) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(m.businessName) LIKE LOWER(CONCAT('%', :search, '%'))
        )
        AND
        (
            :status IS NULL
            OR d.deliveryStatus = :status
        )
    """)
    Page<Delivery> searchDeliveries(
            @Param("search") String search,
            @Param("status") DeliveryStatus status,
            Pageable pageable
    );

}
