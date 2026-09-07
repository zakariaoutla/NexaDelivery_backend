package org.laicose.nexadelivery.repository;

import org.laicose.nexadelivery.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
}
