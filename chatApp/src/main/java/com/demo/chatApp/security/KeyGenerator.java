package com.demo.chatApp.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;

public class KeyGenerator {

    public static void main(String[] args) {
        var key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("Base64 Encoded Secret Key: " + base64Key);
    }
}
