package org.usm.budgetplanner.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuccessfulAuthResponse {

    private String token;

}
