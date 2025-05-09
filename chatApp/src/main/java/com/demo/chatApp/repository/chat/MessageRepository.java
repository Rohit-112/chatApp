package com.demo.chatApp.repository.chat;

import com.demo.chatApp.model.chat.Message;
import com.demo.chatApp.model.chat.Conversation;
import com.demo.chatApp.model.chat.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Get all messages by conversation
    List<Message> findByConversation(Conversation conversation);

    // Get unread messages for a conversation
    List<Message> findByConversationAndReadStatus(Conversation conversation, boolean readStatus);

    // Get all messages sent by a specific user
    List<Message> findBySender(User sender);

    // Get all messages received by a specific user
    List<Message> findByReceiver(User receiver);
}
