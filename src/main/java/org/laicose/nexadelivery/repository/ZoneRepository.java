package org.laicose.nexadelivery.repository;

import org.laicose.nexadelivery.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
    boolean existsByName(String name);
}
