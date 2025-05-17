package com.demo.chatApp.controller;

import com.demo.chatApp.model.dto.ChatHistoryDto;
import com.demo.chatApp.repository.UserRepository;
import com.demo.chatApp.repository.chat.MessageRepository;
import com.demo.chatApp.security.JwtTokenUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatsController {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/history")
    public ResponseEntity<?> getChatHistory(@RequestHeader("Authorization") String token,
                                            @RequestBody ReceiverRequest request) {
        try {
            System.out.println("Request for history");
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Missing or Invalid token");
            }

            String jwt = token.substring(7);
            String username = jwtTokenUtil.getUsernameFromToken(jwt);

            if (username == null) {
                return ResponseEntity.status(401).body("Invalid token or username not found");
            }

            List<ChatHistoryDto> history = messageRepository.getChatHistory(username, request.getReceiverId());
            return ResponseEntity.ok(history);


        }catch (Exception e) {
            System.out.println("Chat History Api error" + e);
            return ResponseEntity.status(500).body("Failed to fetch chat history");
        }
    }

    @Data
    public static class ReceiverRequest {
        private Long receiverId;
    }
}
