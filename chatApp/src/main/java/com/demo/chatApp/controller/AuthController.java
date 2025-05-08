package com.demo.chatApp.controller;

import com.demo.chatApp.model.User;
import com.demo.chatApp.model.dto.ApiResponse;
import com.demo.chatApp.model.dto.AuthRequest;
import com.demo.chatApp.model.dto.AuthResponse;
import com.demo.chatApp.model.dto.SignupRequest;
import com.demo.chatApp.security.JwtTokenUtil;
import com.demo.chatApp.service.UserService;
import com.demo.chatApp.util.WebSocketEventListener;
import com.demo.chatApp.util.WebSocketSessionTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Allow requests from any origin for simplicity, adjust as needed
public class AuthController {

    private final UserService userService;
    @Autowired
    private WebSocketSessionTracker sessionTracker;
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
            User existingUser = userService.getByUsernameForSignup(request.getUsername());
            if (existingUser != null) {
                return ResponseEntity.status(409).body(new ApiResponse<>("Username already exists", 409));
            }

            User user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(request.getPassword())
                    .build();

            User savedUser = userService.registerUser(user);

            return ResponseEntity.status(201).body(new ApiResponse<>(savedUser));

        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ApiResponse<>(e.getMessage(), 400));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> loginUser(@RequestBody AuthRequest request) {
        try {
            User user = userService.getByUsername(request.getUsername());
            System.out.println("Fetched user: " + user);

            if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                System.out.println("Password matched for user: " + user.getUsername());
                String token = jwtTokenUtil.generateToken(user.getUsername());
                return ResponseEntity.ok(new ApiResponse<>(new AuthResponse(token)));
            } else {
                System.out.println("Invalid password or user not found");
                return ResponseEntity.status(401).body(new ApiResponse<>("Invalid username or password", 401));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>("Internal server error", 500));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<?>> getAllUsername(){
        System.out.println("requst for all users");
        try {
            List<User> users = userService.getAllUsers();

            List<Map<String, Object>> usernamesWithStatus = users.stream()
                    .map(user -> {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("username", user.getUsername());
                        userMap.put("online", sessionTracker.isOnline(user.getUsername()));
                        return userMap;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new ApiResponse<>(usernamesWithStatus));
        } catch (RuntimeException e) {
            System.out.println("All Users Api" + e.getMessage());
            return ResponseEntity.status(500).body(new ApiResponse<>("Failed to fetch users", 500));
        }
    }
}
