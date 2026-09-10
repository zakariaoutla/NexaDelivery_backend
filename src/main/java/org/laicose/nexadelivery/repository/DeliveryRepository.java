package org.laicose.nexadelivery.repository;

import org.laicose.nexadelivery.model.Delivery;
import org.laicose.nexadelivery.model.Driver;
import org.laicose.nexadelivery.model.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findByTrackingCode(String trackingCode);

    Page<Delivery> findByMerchant(Merchant merchant, Pageable pageable);
    Page<Delivery> findByDriver(Driver driver, Pageable pageable);



}
