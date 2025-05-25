package org.usm.budgetplanner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.usm.budgetplanner.domain.CategoryEntity;
import org.usm.budgetplanner.domain.TransactionEntity;
import org.usm.budgetplanner.domain.UserEntity;
import org.usm.budgetplanner.dto.request.TransactionDTO;
import org.usm.budgetplanner.dto.response.BalanceUpdateResponse;
import org.usm.budgetplanner.dto.response.TransactionResponse;
import org.usm.budgetplanner.exception.ApplicationException;
import org.usm.budgetplanner.repository.CategoriesRepository;
import org.usm.budgetplanner.repository.TransactionsRepository;
import org.usm.budgetplanner.repository.UsersRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionsRepository transactionsRepository;
    private final UsersRepository usersRepository;
    private final CategoriesRepository categoriesRepository;

    public List<TransactionResponse> getTransactions() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = usersRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return transactionsRepository.findAllByUserId(user.getId())
                .stream()
                .map(t -> TransactionResponse.builder()
                        .category(t.getCategory().getName())
                        .value(t.getValue().doubleValue())
                        .isIncome(t.getCategory().isIncome())
                        .time(t.getTime().atZone(ZoneOffset.UTC).toLocalDateTime())
                        .build())
                .sorted(Comparator.comparing(TransactionResponse::getTime).reversed())
                .toList();
    }

    public BalanceUpdateResponse addTransaction(TransactionDTO transactionDTO) {
        TransactionEntity transactionEntity = new TransactionEntity();

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = usersRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        transactionEntity.setUser(user);

        double value = transactionDTO.getValue();
        value = transactionDTO.isIncome() ? value : -value;
        transactionEntity.setValue(BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP));

        CategoryEntity category = categoriesRepository.findByNameAndIsIncome(transactionDTO.getCategory(), transactionDTO.isIncome())
                .orElseThrow(() -> new ApplicationException("Category not found"));
        transactionEntity.setCategory(category);

        if (transactionDTO.getTime() != null) {
            transactionEntity.setTime(transactionDTO.getTime().toInstant(ZoneOffset.UTC));
        } else {
            transactionEntity.setTime(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        }

        transactionsRepository.save(transactionEntity);

        double newBalance = transactionsRepository.findAllByUserId(user.getId())
                .stream()
                .mapToDouble(t -> t.getValue().doubleValue())
                .sum();
        return BalanceUpdateResponse.builder()
                .newBalance(BigDecimal.valueOf(newBalance))
                .build();
    }

}
