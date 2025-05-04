package com.demo.chatApp.controller;

import com.demo.chatApp.model.User;
import com.demo.chatApp.model.dto.AuthRequest;
import com.demo.chatApp.model.dto.AuthResponse;
import com.demo.chatApp.model.dto.SignupRequest;
import com.demo.chatApp.security.JwtTokenUtil;
import com.demo.chatApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Allow requests from any origin for simplicity, adjust as needed
public class AuthController {

    private final UserService userService;

    private final JwtTokenUtil jwtTokenUtil;
    private SignupRequest signupRequest;
    private AuthRequest authRequest;
    private AuthResponse authResponse;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest request) {
        try {
            User user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(request.getPassword())
                    .build();

            User savedUser = userService.registerUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthRequest request) {
        User user = userService.getByUsername(request.getUsername());
        System.out.println("User:" + user);
        if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            System.out.println("Password: " + user.getPassword().toString());
            String token = jwtTokenUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @GetMapping("/api/token/{username}")
    public String generateToken(@PathVariable String username) {
        return jwtTokenUtil.getUsernameFromToken(username);
    }
}
