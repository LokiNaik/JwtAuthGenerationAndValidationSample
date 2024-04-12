package org.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@Slf4j
public class HomeController {

    @GetMapping(value = "/home")
    public String hello() {
        log.info("Inside home controller !! ");
        return "Hello from home controller";
    }
}
