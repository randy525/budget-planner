package org.usm.budgetplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.usm.budgetplanner.domain.GoalEntity;

import java.util.List;

public interface GoalRepository extends JpaRepository<GoalEntity, Long> {

    @Query("SELECT g FROM GoalEntity g " +
            "JOIN FETCH g.user u " +
            "WHERE u.id = :userId")
    List<GoalEntity> findAllByUserId(Long userId);

}
