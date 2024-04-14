package org.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.auth.config.JwtTokenUtil;
import org.auth.config.WebSecurityConfig;
import org.auth.model.JwtRequest;
import org.auth.model.JwtResponse;
import org.auth.model.MessageResponse;
import org.auth.model.User;
import org.auth.repository.UserDetailsJwtService;
import org.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final UserRepository userRepository;
    @Autowired
    WebSecurityConfig webSecurityConfig;

    /**
     * @param jwtTokenUtil          The JwtTokenUtil.
     * @param userDetailsService    The UserDetailsService interface.
     * @param userDetailsJwtService The UserDetailsJwtService user.
     */
    @Autowired
    public AuthController(JwtTokenUtil jwtTokenUtil, UserRepository userRepository,
                          UserDetailsService userDetailsService, UserDetailsJwtService userDetailsJwtService,
                          AuthenticationManager authenticationManager) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.userDetailsJwtService = userDetailsJwtService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
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
        log.info("Token generated : {} ", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * @param request The request payload
     * @return The JwtToken.
     * @throws Exception The Exception.
     */
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest request) {
        log.info("Inside login() ");
        final User user = userDetailsJwtService.findUserByName(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Not found in db with name " + request.getUsername()));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getName());
        log.info("UserDetails : {} ", userDetails);

        // Authentication and generating JwtToken
        final JwtResponse response = new JwtResponse();
        if (passwordEncoder().matches(request.getPassword(), user.getPassword())) {
            response.setJwtToken(jwtTokenUtil.generateToken(userDetails));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body("Incorrect Password !!");
        }
    }

    /**
     * @param request the User details.
     * @return The JWTToken.
     * @throws Exception Exception.
     */
    @PostMapping(value = "/sign-up")
    public ResponseEntity<?> signup(@RequestBody User request) throws Exception {
        log.info("Inside sign up() ");
        if (Boolean.TRUE.equals(userRepository.name(request.getName()))) {
            log.info("User do exists in the DB !! ");
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (Boolean.TRUE.equals(userRepository.email(request.getEmail()))) {
            log.info("Email do exists in the DB !! ");
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already registered!"));
        }
        // Hashing the password before saving into DB.
        String hashedPassword = passwordEncoder().encode(request.getPassword());
        request.setPassword(hashedPassword);
        //Storing user to DB
        User user = userDetailsJwtService.save(request);
        // Loading userByUsername
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getName());
        // Generating Auth Token
        final JwtResponse response = new JwtResponse();
        response.setJwtToken(jwtTokenUtil.generateToken(userDetails));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * The authentication Helper method.
     */
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

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
