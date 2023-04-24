package com.banca.demo.controller;

import com.banca.demo.model.User;
import com.banca.demo.service.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collection;

@RestController
public class LoginController {

    final ReactiveUserDetailsService users;
    final SecurityService securityService;
    final PasswordEncoder encoder;

    public LoginController(ReactiveUserDetailsService users, SecurityService securityService, PasswordEncoder encoder) {
        this.users = users;
        this.securityService = securityService;
        this.encoder = encoder;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestBody User user) {
        Mono<UserDetails> foundUser = users.findByUsername(user.getName())
                .defaultIfEmpty(new UserDetails() {
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

        return foundUser.map(
                u -> {
                    if (u.getUsername() == null) {
                        return ResponseEntity
                                .status(404)
                                .body("User not found");
                    }

                    if (encoder.matches(user.getPassword(), u.getPassword())) {
                        return ResponseEntity.ok(securityService.generate(u.getUsername()));
                    }

                    return ResponseEntity.badRequest().body("Credentials invalid");
                }
        );

        /*foundUser.flatMap(u -> {
            if (u != null) {
                if (encoder.matches(user.getPassword(), u.getPassword())) {
                    return Mono.just(ResponseEntity
                            .ok(securityService.generate(u.getUsername())));
                }
                return Mono.just(ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid Credentials"));
            }
            return  Mono.just(ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED)
                    .body("User not found"));
        });
        return null;
    }*/
    }
}
