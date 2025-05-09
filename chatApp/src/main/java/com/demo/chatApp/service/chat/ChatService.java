package com.demo.chatApp.service.chat;

import com.demo.chatApp.model.chat.Conversation;
import com.demo.chatApp.model.chat.Message;
import com.demo.chatApp.model.chat.User;
import com.demo.chatApp.repository.chat.ConversationRepository;
import com.demo.chatApp.repository.chat.MessageRepository;
import com.demo.chatApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public Conversation getOrCreateConversation(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Conversation existing = conversationRepository
                .findByUser1AndUser2OrUser2AndUser1(sender, receiver, sender, receiver);

        if (existing != null) {
            return existing;
        }

        Conversation newConversation = Conversation.builder()
                .user1(sender)
                .user2(receiver)
                .build();

        return conversationRepository.save(newConversation);
    }

    public Message saveMessage(String senderName, String receiverName, String messageContent) {

        User sender = userRepository.findByUsername(senderName)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findByUsername(receiverName)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Conversation conversation = getOrCreateConversation(sender.getId(), receiver.getId());

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .receiver(receiver)
                .content(messageContent)
                .timestamp(LocalDateTime.parse(LocalDateTime.now().toString()))
                .readStatus(false)
                .build();

        return messageRepository.save(message);
    }

    public List<Message> getMessagesBetweenUsers(Long user1Id, Long user2Id) {
        Conversation conversation = getOrCreateConversation(user1Id, user2Id);
        return messageRepository.findByConversation(conversation);
    }

    @Transactional
    public void markMessagesAsRead(Long conversationId, Long userId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        List<Message> messages = messageRepository.findByConversationAndReadStatus(conversation, false);

        for (Message msg : messages) {
            if (!msg.getSender().getId().equals(userId)) {
                msg.setReadStatus(true);
                messageRepository.save(msg);
            }
        }
    }
}
