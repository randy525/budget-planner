package org.usm.budgetplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.usm.budgetplanner.domain.CategoryEntity;

import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByNameAndIncome(String name, boolean isIncome);

}
