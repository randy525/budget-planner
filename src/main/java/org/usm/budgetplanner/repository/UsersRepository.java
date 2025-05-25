package org.usm.budgetplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.usm.budgetplanner.domain.UserEntity;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

}
