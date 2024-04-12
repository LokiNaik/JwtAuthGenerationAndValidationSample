package org.auth.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class NoAuthController {

    @GetMapping(value = "/no-auth")
    public String noAuth() {
        return "Inside No Auth Controller !!";
    }
}
