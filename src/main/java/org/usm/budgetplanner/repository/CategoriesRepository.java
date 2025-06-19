package org.usm.budgetplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.usm.budgetplanner.domain.CategoryEntity;

public interface CategoriesRepository extends JpaRepository<CategoryEntity, Long> {

}
