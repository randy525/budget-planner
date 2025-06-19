package org.usm.budgetplanner.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {

    private long id;
    private String name;
    private String icon;
    @JsonProperty("isIncome")
    private boolean isIncome;

}
