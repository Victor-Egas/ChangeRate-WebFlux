package com.banca.demo.security;

import com.banca.demo.service.SecurityService;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Component
public class AuthManager implements ReactiveAuthenticationManager {

    final SecurityService securityService;
    final ReactiveUserDetailsService users;

    public AuthManager(SecurityService securityService, ReactiveUserDetailsService users) {
        this.securityService = securityService;
        this.users = users;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .cast(BearerTocken.class)
                .flatMap(auth -> {
                    String userName = securityService.getUserName(auth.getCredentials());
                    Mono<UserDetails> foundUser = users.findByUsername(userName).defaultIfEmpty(new UserDetails() {
                        @Override
                        public Collection<? extends GrantedAuthority> getAuthorities() {
                            return null;
                        }

                        @Override
                        public String getPassword() {
                            return null;
                        }

                        @Override
                        public String getUsername() {
                            return null;
                        }

                        @Override
                        public boolean isAccountNonExpired() {
                            return false;
                        }

                        @Override
                        public boolean isAccountNonLocked() {
                            return false;
                        }

                        @Override
                        public boolean isCredentialsNonExpired() {
                            return false;
                        }

                        @Override
                        public boolean isEnabled() {
                            return false;
                        }
                    });

                    Mono<Authentication> authenticationUser= foundUser
                            .flatMap(u -> {
                                if (u.getUsername() == null) {
                                    Mono.error(new IllegalArgumentException("User not found in auth manager"));
                                }

                                if (securityService.validate(u, auth.getCredentials())) {
                                    return Mono.justOrEmpty(
                                            new UsernamePasswordAuthenticationToken(
                                                    u.getUsername(), u.getPassword(), u.getAuthorities()));
                                }

                                Mono.error(new IllegalArgumentException("Invalid / Expired token"));
                                return Mono.justOrEmpty(
                                        new UsernamePasswordAuthenticationToken(
                                                u.getUsername(), u.getPassword(), u.getAuthorities()));
                            });
                    return authenticationUser;
                });
    }
}
