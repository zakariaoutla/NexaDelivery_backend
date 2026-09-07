package org.laicose.nexadelivery.repository;

import org.laicose.nexadelivery.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
}
