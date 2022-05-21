package com.laioffer.travelplanner.controller;

import com.laioffer.travelplanner.model.User;
import com.laioffer.travelplanner.model.UserRole;
import com.laioffer.travelplanner.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {
    private RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public void addUser(@RequestBody User user) {
        registerService.add(user, UserRole.ROLE_USER);
    }

}
