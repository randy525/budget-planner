package org.usm.budgetplanner.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransactionUpdateResponse {

    private TransactionResponse transaction;
    private BigDecimal newBalance;

}
