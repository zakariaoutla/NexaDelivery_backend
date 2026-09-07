package org.laicose.nexadelivery.repository;

import org.laicose.nexadelivery.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
