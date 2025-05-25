package org.usm.budgetplanner.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UserLoginDTO {

    private String email;
    private String password;

}
