package org.laicose.nexadelivery.repository;

import org.laicose.nexadelivery.Enum.DriverStatus;
import org.laicose.nexadelivery.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByEmail(String email);


    long countByDriverStatus(DriverStatus driverStatus);

    
    List<Driver> findByDriverStatus(
            DriverStatus driverStatus
    );

    @Query("""
    SELECT d
    FROM Driver d
    WHERE
        (
            :search IS NULL
            OR LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(d.email) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(d.telephone) LIKE LOWER(CONCAT('%', :search, '%'))
        )
        AND
        (
            :status IS NULL
            OR d.driverStatus = :status
        )
    """)
    Page<Driver> searchDrivers(
            @Param("search") String search,
            @Param("status") DriverStatus status,
            Pageable pageable
    );

}
