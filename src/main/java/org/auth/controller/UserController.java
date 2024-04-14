package org.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.auth.model.User;
import org.auth.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/user")
public class UserController {

    private JwtUserDetailsService userDetailsService;

    @Autowired
    public UserController(JwtUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping(value = "/home")
    public String hello() {
        log.info("Inside home controller !! ");
        return "Hello from home controller";
    }


    @GetMapping("/get-users")
    public ResponseEntity<List<User>> fetchAllUsers() {
        List<User> userList = userDetailsService.fetchAllUsers();
        return new ResponseEntity<>(userList, HttpStatusCode.valueOf(200));
    }

}
