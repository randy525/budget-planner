package org.usm.budgetplanner.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class GoalResponse {
    long id;
    String name;
    String icon;
    BigDecimal currentAmount;
    BigDecimal goalAmount;
    BigDecimal percentAchieved;
    boolean isDone;
}
