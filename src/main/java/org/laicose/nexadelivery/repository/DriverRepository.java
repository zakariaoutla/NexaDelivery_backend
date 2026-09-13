package org.laicose.nexadelivery.repository;

import org.laicose.nexadelivery.Enum.DriverStatus;
import org.laicose.nexadelivery.model.Driver;
import org.laicose.nexadelivery.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByEmail(String email);
    List<Driver> findByZoneAndDriverStatus(
            Zone zone,
            DriverStatus driverStatus
    );

}
