package org.laicose.nexadelivery.repository;

import org.laicose.nexadelivery.model.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Optional<Merchant> findByEmail(String email);

    @Query("""
    SELECT m
    FROM Merchant m
    WHERE
        :search IS NULL
        OR LOWER(m.name) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(m.email) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(m.telephone) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(m.businessName) LIKE LOWER(CONCAT('%', :search, '%'))
    """)
    Page<Merchant> searchMerchants(
            @Param("search") String search,
            Pageable pageable
    );

}
