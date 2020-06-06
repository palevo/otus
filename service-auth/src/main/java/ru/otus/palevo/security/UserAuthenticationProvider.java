package ru.otus.palevo.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ru.otus.palevo.model.User;
import ru.otus.palevo.model.UserRole;
import ru.otus.palevo.service.UserService;

/**
 * {@link AuthenticationProvider} implementation
 *
 * @author A.Osipov
 * @since 06 июнь 2020 г.
 */
@Service
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final UserService service;

    /**
     * Constructor
     */
    public UserAuthenticationProvider(UserService service) {
        this.service = service;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName().trim();
        String password = authentication.getCredentials().toString().trim();

        User user = service.login(username, password).block();
        if (null != user) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>(user.getRoles().size());
            for (UserRole role : user.getRoles()) {
                grantedAuthorities.add((GrantedAuthority) () -> "ROLE_" + role.getCode());
            }

            // The "User" class is provided by Spring and represents a model class for user to be returned by UserDetailsService
            // And used by auth manager to verify and check user authentication.
            return new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), grantedAuthorities);
        }

        // If user not found. Throw this exception.
        throw new UsernameNotFoundException("Username: " + username + " not found");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
