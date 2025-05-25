package org.usm.budgetplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.usm.budgetplanner.domain.TransactionEntity;

import java.util.List;

public interface TransactionsRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("SELECT t FROM TransactionEntity t " +
            "JOIN FETCH t.user u " +
            "JOIN FETCH t.category " +
            "WHERE u.id = :userId")
    List<TransactionEntity> findAllByUserId(Long userId);

}
