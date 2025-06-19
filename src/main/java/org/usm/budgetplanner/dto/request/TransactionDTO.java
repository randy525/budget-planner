package org.usm.budgetplanner.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class TransactionDTO {

    private Long categoryId;

    private double value;

    private LocalDateTime time;

}
