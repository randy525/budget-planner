package org.usm.budgetplanner.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.usm.budgetplanner.domain.UserEntity;
import org.usm.budgetplanner.dto.request.UserLoginDTO;
import org.usm.budgetplanner.dto.request.UserRegisterDTO;
import org.usm.budgetplanner.dto.response.SuccessfulAuthResponse;
import org.usm.budgetplanner.exception.ApplicationException;
import org.usm.budgetplanner.repository.UsersRepository;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationProvider authenticationProvider;

    public SuccessfulAuthResponse signUp(UserRegisterDTO user) {
        if (usersRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ApplicationException("A user with this email already exists");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setName(user.getName());
        Instant now = Instant.now();
        userEntity.setCreatedAt(now);
        userEntity.setUpdatedAt(now);

        usersRepository.save(userEntity);

        return SuccessfulAuthResponse.builder()
                .token(jwtService.generateToken(userEntity.getEmail()))
                .build();
    }

    public SuccessfulAuthResponse login(UserLoginDTO loginDetails) {
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(loginDetails.getEmail(), loginDetails.getPassword());
        Authentication authentication = authenticationProvider.authenticate(token);

        if (authentication.isAuthenticated()) {
            return SuccessfulAuthResponse.builder()
                    .token(jwtService.generateToken(loginDetails.getEmail()))
                    .build();
        } else {
            throw new ApplicationException("Invalid username or password");
        }
    }

}
