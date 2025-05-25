package org.usm.budgetplanner.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.usm.budgetplanner.dto.request.UserLoginDTO;
import org.usm.budgetplanner.dto.request.UserRegisterDTO;
import org.usm.budgetplanner.dto.response.SuccessfulAuthResponse;
import org.usm.budgetplanner.security.AuthenticationService;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public SuccessfulAuthResponse signUp(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {
        return authenticationService.signUp(userRegisterDTO);
    }

    @PostMapping("/login")
    public SuccessfulAuthResponse login(@RequestBody UserLoginDTO userLoginDTO) {
        return authenticationService.login(userLoginDTO);
    }


}
