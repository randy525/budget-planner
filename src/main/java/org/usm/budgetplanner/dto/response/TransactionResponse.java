package org.usm.budgetplanner.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {

    private String category;
    private double value;
    private boolean isIncome;
    private LocalDateTime time;

}
