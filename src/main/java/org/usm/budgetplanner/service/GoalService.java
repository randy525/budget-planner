package org.usm.budgetplanner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.usm.budgetplanner.domain.GoalEntity;
import org.usm.budgetplanner.domain.UserEntity;
import org.usm.budgetplanner.dto.request.GoalDTO;
import org.usm.budgetplanner.dto.response.GoalResponse;
import org.usm.budgetplanner.exception.ApplicationException;
import org.usm.budgetplanner.repository.GoalRepository;
import org.usm.budgetplanner.repository.UsersRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final UsersRepository usersRepository;

    public List<GoalResponse> getGoals() {
        UserEntity currentUser = getCurrentUser();
        return goalRepository.findAllByUserId(currentUser.getId()).stream()
                .map(this::toDto)
                .sorted(Comparator.comparing(GoalResponse::getId))
                .collect(Collectors.toList());
    }

    public GoalResponse addGoal(GoalDTO goalDTO) {
        GoalEntity goalEntity = new GoalEntity();
        UserEntity user = getCurrentUser();

        goalEntity.setUser(user);
        goalEntity.setName(goalDTO.getName());
        goalEntity.setIcon(goalDTO.getIcon());
        goalEntity.setCurrentAmount(BigDecimal.ZERO);
        goalEntity.setGoalAmount(goalDTO.getGoalAmount());
        goalEntity.setDone(false);

        goalRepository.save(goalEntity);

        return toDto(goalEntity);
    }

    public GoalResponse updateCurrentAmount(Long id, GoalDTO goalDTO) {
        UserEntity currentUser = getCurrentUser();

        GoalEntity goalEntity = goalRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Goal not found"));

        if (!goalEntity.getUser().equals(currentUser)) {
            throw new ApplicationException("User is not authorized to modify this goal");
        }

        goalEntity.setCurrentAmount(goalEntity.getCurrentAmount().add(goalDTO.getGoalAmount()));
        goalEntity.setDone(goalEntity.getCurrentAmount().compareTo(goalEntity.getGoalAmount()) >= 0);

        goalRepository.save(goalEntity);

        return toDto(goalEntity);
    }

    public void deleteGoalById(Long id) {
        UserEntity currentUser = getCurrentUser();

        GoalEntity goalEntity = goalRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Goal not found"));

        if (!goalEntity.getUser().equals(currentUser)) {
            throw new ApplicationException("User is not authorized to modify this goal");
        }

        goalRepository.delete(goalEntity);
    }

    private GoalResponse toDto(GoalEntity goalEntity) {
        return GoalResponse.builder()
                .id(goalEntity.getId())
                .name(goalEntity.getName())
                .icon(goalEntity.getIcon())
                .currentAmount(goalEntity.getCurrentAmount())
                .goalAmount(goalEntity.getGoalAmount())
                .isDone(goalEntity.isDone())
                .percentAchieved(calculatePercentAchieved(goalEntity.getCurrentAmount(), goalEntity.getGoalAmount()))
                .build();
    }

    private BigDecimal calculatePercentAchieved(BigDecimal currentAmount, BigDecimal goalAmount) {
        return currentAmount.divide(goalAmount, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usersRepository.findByEmail(email).orElseThrow(() -> new ApplicationException("User not found"));
    }

}
