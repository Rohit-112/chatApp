package com.demo.chatApp.repository.chat;

import com.demo.chatApp.model.chat.Message;
import com.demo.chatApp.model.chat.Conversation;
import com.demo.chatApp.model.chat.User;
import com.demo.chatApp.model.dto.ChatHistoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Get all messages by conversation
    List<Message> findByConversation(Conversation conversation);

    // Get unread messages for a conversation
    List<Message> findByConversationAndReadStatus(Conversation conversation, boolean readStatus);

    @Query("""
    SELECT new com.demo.chatApp.model.dto.ChatHistoryDto(
        m.id, m.content, m.timestamp,
        m.sender.id, m.sender.username,
        m.receiver.id, m.receiver.username
    )
    FROM Message m
    WHERE (m.sender.username = :username AND m.receiver.id = :receiverId)
       OR (m.sender.id = :receiverId AND m.receiver.username = :username)
    ORDER BY m.timestamp ASC
""")
    List<ChatHistoryDto> getChatHistory(@Param("username") String username, @Param("receiverId") Long receiverId);

}
