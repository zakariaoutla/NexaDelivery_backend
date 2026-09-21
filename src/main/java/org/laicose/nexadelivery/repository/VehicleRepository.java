package org.laicose.nexadelivery.repository;

import org.laicose.nexadelivery.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query("""
    SELECT v
    FROM Vehicle v
    WHERE NOT EXISTS (
        SELECT d.id
        FROM Driver d
        WHERE d.vehicle = v
    )
    """)
    Page<Vehicle> findAvailableVehicles(Pageable pageable);
}
