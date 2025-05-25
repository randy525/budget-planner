package org.usm.budgetplanner.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class TransactionDTO {

    private String category;

    private double value;

    @JsonProperty("isIncome")
    private boolean isIncome;

    private LocalDateTime time;

}
