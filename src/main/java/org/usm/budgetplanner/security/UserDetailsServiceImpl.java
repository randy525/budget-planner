package org.usm.budgetplanner.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.usm.budgetplanner.domain.UserEntity;
import org.usm.budgetplanner.repository.UsersRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity entity = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        return new UserAuthDetails(entity.getEmail(), entity.getPassword());
    }

}
