package org.usm.budgetplanner.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.usm.budgetplanner.dto.request.GoalDTO;
import org.usm.budgetplanner.dto.response.GoalResponse;
import org.usm.budgetplanner.service.GoalService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/goal")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @GetMapping("/list")
    public List<GoalResponse> getGoals() {
        return goalService.getGoals();
    }

    @PostMapping("/add")
    public GoalResponse addGoal(@RequestBody GoalDTO goalDTO) {
        return goalService.addGoal(goalDTO);
    }

    @PostMapping("/{goalId}")
    public GoalResponse updateGoalAmount(@PathVariable Long goalId, @RequestBody GoalDTO goalDTO) {
        return goalService.updateCurrentAmount(goalId, goalDTO);
    }

    @DeleteMapping("/{goalId}")
    public ResponseEntity<?> deleteGoal(@PathVariable Long goalId) {
        goalService.deleteGoalById(goalId);
        return ResponseEntity.noContent().build();
    }


}
