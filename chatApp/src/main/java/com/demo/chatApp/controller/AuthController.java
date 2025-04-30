package com.demo.chatApp.controller;

import com.demo.chatApp.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthController(JwtTokenUtil jwtTokenUtil){
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/api/token/{username}")
    public String generateToken(@PathVariable String username){
        return jwtTokenUtil.getUsernameFromToken(username);
    }
}
