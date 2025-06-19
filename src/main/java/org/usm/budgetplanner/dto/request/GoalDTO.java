package org.usm.budgetplanner.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Getter
@Setter
public class GoalDTO {

    private String name;
    private String icon;
    private BigDecimal goalAmount;

}
