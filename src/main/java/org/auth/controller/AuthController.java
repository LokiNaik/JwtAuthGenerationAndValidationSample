package org.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.auth.config.JwtTokenUtil;
import org.auth.model.JwtRequest;
import org.auth.model.JwtResponse;
import org.auth.model.User;
import org.auth.repository.UserDetailsJwtService;
import org.auth.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final UserDetailsJwtService userDetailsJwtService;

    /**
     * @param jwtTokenUtil          The JwtTokenUtil.
     * @param userDetailsService    The UserDetailsService interface.
     * @param userDetailsJwtService The UserDetailsJwtService user.
     */
    @Autowired
    AuthController(JwtTokenUtil jwtTokenUtil,
                   UserDetailsService userDetailsService, UserDetailsJwtService userDetailsJwtService,
                   AuthenticationManager authenticationManager) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.userDetailsJwtService = userDetailsJwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * @param request The UserDetails.
     * @return The JwtToken.
     * @throws Exception The Exception.
     */
    @PostMapping(value = "/auth")
    public ResponseEntity<JwtResponse> generateToken(@RequestBody JwtRequest request) throws Exception {
        authenticate(request.getUsername(), (request.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final JwtResponse response = new JwtResponse();
        response.setJwtToken(jwtTokenUtil.generateToken(userDetails));
        log.info("Token generated : {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * @param request the User details.
     * @return The JWTToken.
     * @throws Exception Exception.
     */
    @PostMapping(value = "/login")
    public ResponseEntity<JwtResponse> login(@RequestBody User request) throws Exception {
        return new ResponseEntity<>(auth(userDetailsJwtService.save(request)), HttpStatus.OK);
    }

    private JwtResponse auth(User user) throws Exception {
        authenticate(user.getName(), user.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getName());
        final JwtResponse response = new JwtResponse();
        response.setJwtToken(jwtTokenUtil.generateToken(userDetails));
        log.info("Token generated : {}", response);
        return response;
    }

    private void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
