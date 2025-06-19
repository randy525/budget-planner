package org.usm.budgetplanner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.usm.budgetplanner.domain.CategoryEntity;
import org.usm.budgetplanner.domain.TransactionEntity;
import org.usm.budgetplanner.domain.UserEntity;
import org.usm.budgetplanner.dto.request.TransactionDTO;
import org.usm.budgetplanner.dto.response.TransactionResponse;
import org.usm.budgetplanner.dto.response.TransactionUpdateResponse;
import org.usm.budgetplanner.exception.ApplicationException;
import org.usm.budgetplanner.repository.CategoriesRepository;
import org.usm.budgetplanner.repository.TransactionsRepository;
import org.usm.budgetplanner.repository.UsersRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionsRepository transactionsRepository;
    private final UsersRepository usersRepository;
    private final CategoriesRepository categoriesRepository;

    private final InfoService infoService;

    public Map<LocalDate, List<TransactionResponse>> getTransactions() {
        UserEntity user = getCurrentUser();
        var transactions = transactionsRepository.findAllByUserId(user.getId())
                .stream()
                .map(t -> TransactionResponse.builder()
                        .id(t.getId())
                        .categoryIcon(t.getCategory().getIcon())
                        .categoryName(t.getCategory().getName())
                        .value(t.getValue().doubleValue())
                        .isIncome(t.getCategory().isIncome())
                        .time(t.getTime().atZone(ZoneOffset.UTC).toLocalDateTime())
                        .build())
                .collect(Collectors.groupingBy(
                        t -> t.getTime().toLocalDate(),
                        () -> new TreeMap<LocalDate, List<TransactionResponse>>(Comparator.reverseOrder()),
                        Collectors.toList()
                ));
        transactions
                .forEach((date, transactionList)
                        -> transactionList.sort(Comparator.comparing(TransactionResponse::getTime).reversed()));
        return transactions;
    }

    public TransactionUpdateResponse addTransaction(TransactionDTO transactionDTO) {
        TransactionEntity transactionEntity = new TransactionEntity();

        UserEntity user = getCurrentUser();
        transactionEntity.setUser(user);

        CategoryEntity category = categoriesRepository.findById(transactionDTO.getCategoryId())
                .orElseThrow(() -> new ApplicationException("Category not found"));
        transactionEntity.setCategory(category);

        double value = transactionDTO.getValue();
        value = category.isIncome() ? value : -value;
        transactionEntity.setValue(BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP));

        if (transactionDTO.getTime() != null) {
            transactionEntity.setTime(transactionDTO.getTime().toInstant(ZoneOffset.UTC));
        } else {
            transactionEntity.setTime(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        }

        transactionsRepository.save(transactionEntity);

        return TransactionUpdateResponse.builder()
                .transaction(toDto(transactionEntity))
                .newBalance(infoService.getBalance())
                .build();
    }

    public TransactionUpdateResponse updateTransaction(Long transactionId, TransactionDTO transactionDTO) {
        UserEntity user = getCurrentUser();
        TransactionEntity transaction = transactionsRepository
                .findById(transactionId)
                .orElseThrow(() -> new ApplicationException("Transaction not found"));

        checkAccessToTransaction(transaction, user);

        CategoryEntity category = categoriesRepository.findById(transactionDTO.getCategoryId())
                .orElseThrow(() -> new ApplicationException("Category not found"));
        transaction.setValue(BigDecimal.valueOf(transactionDTO.getValue()).setScale(2, RoundingMode.HALF_UP));
        transaction.setCategory(category);

        transactionsRepository.save(transaction);

        return TransactionUpdateResponse.builder()
                .transaction(toDto(transaction))
                .newBalance(infoService.getBalance())
                .build();
    }

    public void deleteTransactionById(Long transactionId) {
        UserEntity user = getCurrentUser();
        TransactionEntity transaction = transactionsRepository
                .findById(transactionId)
                .orElseThrow(() -> new ApplicationException("Transaction not found"));

        checkAccessToTransaction(transaction, user);

        transactionsRepository.delete(transaction);
    }

    private TransactionResponse toDto(TransactionEntity transactionEntity) {
        return TransactionResponse.builder()
                .id(transactionEntity.getId())
                .categoryIcon(transactionEntity.getCategory().getIcon())
                .categoryName(transactionEntity.getCategory().getName())
                .value(transactionEntity.getValue().doubleValue())
                .isIncome(transactionEntity.getCategory().isIncome())
                .time(transactionEntity.getTime().atZone(ZoneOffset.UTC).toLocalDateTime())
                .build();
    }

    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usersRepository.findByEmail(email).orElseThrow(() -> new ApplicationException("User not found"));
    }

    private void checkAccessToTransaction(TransactionEntity transaction, UserEntity user) {
        if (!transaction.getUser().equals(user)) {
            throw new ApplicationException("User is not authorized to modify this transaction");
        }
    }
}
