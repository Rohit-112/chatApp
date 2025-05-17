package com.demo.chatApp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatHistoryDto {

    private Long messageId;
    private String content;
    private LocalDateTime timestamp;
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String receiverName;
}
