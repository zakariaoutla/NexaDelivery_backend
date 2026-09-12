package org.laicose.nexadelivery.repository;

import org.laicose.nexadelivery.model.CollectionPoint;
import org.laicose.nexadelivery.model.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectionPointRepository extends JpaRepository<CollectionPoint, Long> {
    Page<CollectionPoint> findByMerchant(Merchant merchant, Pageable pageable);
    Optional<CollectionPoint> findByIdAndMerchant(Long id, Merchant merchant);
}
