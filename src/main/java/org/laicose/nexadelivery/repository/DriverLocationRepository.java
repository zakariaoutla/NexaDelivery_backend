package org.laicose.nexadelivery.repository;

import org.laicose.nexadelivery.model.Driver;
import org.laicose.nexadelivery.model.DriverLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DriverLocationRepository extends JpaRepository<DriverLocation, Long> {

    Page<DriverLocation> findByDriverOrderByTimestampDesc(
            Driver driver,
            Pageable pageable
    );

    Optional<DriverLocation> findFirstByDriverOrderByTimestampDesc(Driver driver);

    @Query("""
            SELECT dl
            FROM DriverLocation dl
            WHERE dl.timestamp = (
                SELECT MAX(dl2.timestamp)
                FROM DriverLocation dl2
                WHERE dl2.driver = dl.driver
            )
            """)
    List<DriverLocation> findLatestLocationForEachDriver();


}
