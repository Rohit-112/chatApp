package com.demo.chatApp.controller;

import com.demo.chatApp.model.User;
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
public class AuthController {

    private final UserService userService;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(JwtTokenUtil jwtTokenUtil, UserService userService){
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        System.out.println("springboot" + user);
        try{
            User savedUser = userService.registerUser(user);
            return ResponseEntity.ok(savedUser);
        }catch (RuntimeException e){
            System.out.println("error "+ e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user){
        boolean isValid = userService.validateUser(user.getUsername(),user.getPassword());
        if (isValid){
            String token = jwtTokenUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        }else {
            return ResponseEntity.status(401).body("invalid username or password");
        }
    }

    @GetMapping("/api/token/{username}")
    public String generateToken(@PathVariable String username){
        return jwtTokenUtil.getUsernameFromToken(username);
    }
}
