package org.laicose.nexadelivery.repository;

import org.laicose.nexadelivery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
