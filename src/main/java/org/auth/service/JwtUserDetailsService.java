package org.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.auth.model.User;
import org.auth.repository.UserDetailsJwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {
    private final UserDetailsJwtService userDetailsJwtService;
    @Autowired
    public JwtUserDetailsService(UserDetailsJwtService userDetailsJwtService) {
        this.userDetailsJwtService = userDetailsJwtService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDetailsJwtService.findUserByName(username).orElseThrow(() -> new UsernameNotFoundException("User Not found in db with name " + username));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getName())
                .password(user.getPassword())
                .roles(String.valueOf(user.getRoles()))
                .disabled(false)
                .accountExpired(false)
                .accountLocked(false)
                .authorities(Collections.emptyList())
                .credentialsExpired(false)
                .build();
    }


}
