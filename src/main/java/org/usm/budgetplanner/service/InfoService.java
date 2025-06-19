package org.usm.budgetplanner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.usm.budgetplanner.domain.UserEntity;
import org.usm.budgetplanner.dto.response.CategoryResponse;
import org.usm.budgetplanner.dto.response.UserInfoResponse;
import org.usm.budgetplanner.exception.ApplicationException;
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

    public BigDecimal getBalance() {
        UserEntity user = getCurrentUser();
        double balance = transactionsRepository.findAllByUserId(user.getId())
                .stream()
                .mapToDouble(t -> t.getValue().doubleValue())
                .sum();
        return BigDecimal.valueOf(balance);
    }

    public UserInfoResponse getUserInfo() {
        UserEntity user = getCurrentUser();

        return UserInfoResponse.builder()
                .name(user.getName())
                .balance(getBalance())
                .build();
    }

    public List<CategoryResponse> getCategories() {
        final String IGNORED_CATEGORY = "Adjust balance";
        return categoriesRepository.findAll().stream()
                .filter(c -> !IGNORED_CATEGORY.equals(c.getName()))
                .map(c -> CategoryResponse.builder()
                        .id(c.getId())
                        .icon(c.getIcon())
                        .name(c.getName())
                        .isIncome(c.isIncome())
                        .build())
                .collect(Collectors.toList());

    }

    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usersRepository.findByEmail(email).orElseThrow(() -> new ApplicationException("User not found"));
    }


}
