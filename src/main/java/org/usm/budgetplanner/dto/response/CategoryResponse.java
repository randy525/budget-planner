package org.usm.budgetplanner.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {

    private long id;
    private String name;
    private String icon;
    private boolean isIncome;

}
