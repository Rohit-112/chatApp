package com.demo.chatApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    private MessageType type;
    private String content;
    private String sender;
    private String receiver; // for private message
    private String chatRoomId; // for multiple chat room
    private String timestamp;
}
