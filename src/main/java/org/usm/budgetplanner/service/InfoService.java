package org.usm.budgetplanner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.usm.budgetplanner.domain.UserEntity;
import org.usm.budgetplanner.dto.response.BalanceResponse;
import org.usm.budgetplanner.dto.response.CategoryResponse;
import org.usm.budgetplanner.repository.CategoriesRepository;
import org.usm.budgetplanner.repository.TransactionsRepository;
import org.usm.budgetplanner.repository.UsersRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InfoService {

    private final TransactionsRepository transactionsRepository;
    private final UsersRepository usersRepository;
    private final CategoriesRepository categoriesRepository;

    @Value("${info.urls.icons-base-url}")
    private String iconsBaseURL;

    public BalanceResponse getBalance() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = usersRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        double balance = transactionsRepository.findAllByUserId(user.getId())
                .stream()
                .mapToDouble(t -> t.getValue().doubleValue())
                .sum();

        return BalanceResponse.builder()
                .balance(BigDecimal.valueOf(balance))
                .build();
    }

    public List<CategoryResponse> getCategories() {
        return categoriesRepository.findAll().stream()
                .map(c -> CategoryResponse.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .icon(iconsBaseURL + c.getIcon())
                        .isIncome(c.isIncome())
                        .build())
                .collect(Collectors.toList());

    }


}
