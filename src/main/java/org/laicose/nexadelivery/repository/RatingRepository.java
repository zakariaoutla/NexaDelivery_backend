package org.laicose.nexadelivery.repository;

import org.laicose.nexadelivery.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    boolean existsByDeliveryId(Long deliveryId);

    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.delivery.driver.id = :driverId")
    Double findAverageByDriverId(@Param("driverId") Long driverId);
}
