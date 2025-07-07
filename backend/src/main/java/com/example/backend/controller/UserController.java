package com.example.backend.controller;

import com.example.backend.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest registerRequest) {
        return userService.register(registerRequest.toUser());
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest) {
        userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @Data
    static class RegisterRequest {
        private String name;
        private Integer age;
        private String dateOfBirth;
        private String contactNumber;
        private String country;
        private String state;
        private String city;

        public com.example.backend.entity.User toUser() {
            com.example.backend.entity.User user = new com.example.backend.entity.User();
            user.setName(name);
            user.setAge(age);
            user.setDateOfBirth(dateOfBirth);
            user.setContactNumber(contactNumber);
            user.setCountry(country);
            user.setState(state);
            user.setCity(city);
            return user;
        }
    }

    @Data
    static class LoginRequest {
        private String username;
        private String password;
    }
}
