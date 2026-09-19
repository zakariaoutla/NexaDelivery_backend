package org.laicose.nexadelivery.repository;

import org.laicose.nexadelivery.Enum.DriverStatus;
import org.laicose.nexadelivery.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByEmail(String email);


    long countByDriverStatus(DriverStatus driverStatus);

    
    List<Driver> findByDriverStatus(
            DriverStatus driverStatus
    );

}
