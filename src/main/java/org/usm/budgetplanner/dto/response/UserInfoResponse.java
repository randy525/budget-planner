package org.usm.budgetplanner.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class UserInfoResponse {

    private String name;
    private BigDecimal balance;

}
