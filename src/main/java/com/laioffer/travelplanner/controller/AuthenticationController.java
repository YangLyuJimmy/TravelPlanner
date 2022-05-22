package com.laioffer.travelplanner.controller;

import com.laioffer.travelplanner.model.Token;
import com.laioffer.travelplanner.model.User;
import com.laioffer.travelplanner.model.UserRole;
import com.laioffer.travelplanner.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    public Token authenticateUser(@RequestBody User user) {
        return authenticationService.authenticate(user, UserRole.ROLE_USER);
    }
}
