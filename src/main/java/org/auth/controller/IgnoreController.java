package org.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IgnoreController {

    @GetMapping("/ignore")
    public String home() {
        return "Hi there !!";
    }
}
