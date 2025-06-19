package org.usm.budgetplanner.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {

    private long id;
    private double value;
    private String categoryIcon;
    private String categoryName;
    private boolean isIncome;
    private LocalDateTime time;

}
