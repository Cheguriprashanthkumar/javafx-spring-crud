package com.example.backend.controller;

import com.example.backend.dto.LoginRequest;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/countries")
    public String[] getCountries() {
        return new String[]{"India", "USA"};
    }

    @GetMapping("/states")
    public String[] getStates(@RequestParam String country) {
        if (country.equalsIgnoreCase("India")) {
            return new String[]{"Telangana", "Andhra Pradesh", "Karnataka", "Tamil Nadu"};
        } else if (country.equalsIgnoreCase("USA")) {
            return new String[]{"California", "Texas", "New York", "Florida"};
        }
        return new String[]{};
    }

    @GetMapping("/cities")
    public String[] getCities(@RequestParam String state) {
        if (state.equalsIgnoreCase("Telangana")) {
            return new String[]{"Hyderabad", "Warangal", "Karimnagar"};
        } else if (state.equalsIgnoreCase("Andhra Pradesh")) {
            return new String[]{"Vijayawada", "Guntur", "Vizag"};
        } else if (state.equalsIgnoreCase("California")) {
            return new String[]{"Los Angeles", "San Francisco", "San Diego"};
        } else if (state.equalsIgnoreCase("Texas")) {
            return new String[]{"Houston", "Dallas", "Austin"};
        }
        return new String[]{};
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByName(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return ResponseEntity.ok("Login successful");
    }

}
