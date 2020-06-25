package ru.otus.service.security.basic;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import reactor.core.publisher.Mono;
import ru.otus.common.domain.User;
import ru.otus.common.service.UserService;

import static java.util.stream.Collectors.toSet;

/**
 * User detail service
 *
 * @author A.Osipov
 * @since 24 июнь 2020 г.
 */
public class BasicUserDetailService implements ReactiveUserDetailsService {

    @Getter
    @Setter
    public static class UserFacade extends User implements UserDetails {

        @Delegate
        private User user;

        private String username;
        private Collection<? extends GrantedAuthority> authorities;
        private boolean accountNonExpired = true;
        private boolean accountNonLocked = true;
        private boolean credentialsNonExpired = true;
        private boolean enabled = true;

        /**
         * @param user otus user
         */
        public UserFacade(User user) {
            this.user = user;
            this.username = user.getName();
            this.authorities = user.getRoles().stream().map(r ->
                    new SimpleGrantedAuthority("ROLE_" + r.getCode())).collect(toSet());
        }
    }

    private final UserService service;

    /**
     * @param service user service
     */
    public BasicUserDetailService(UserService service) {
        this.service = service;
    }

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        return service.byEmail(email).map(UserFacade::new);
    }
}
