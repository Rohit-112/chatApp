package com.demo.chatApp.repository.chat;

import com.demo.chatApp.model.chat.Conversation;
import com.demo.chatApp.model.chat.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    // Find all conversations where a user is involved
    List<Conversation> findByUser1OrUser2(User user1, User user2);

    // Find specific conversation between two users
    Conversation findByUser1AndUser2(User user1, User user2);
    Conversation findByUser1AndUser2OrUser2AndUser1(User user1, User user2, User user3, User user4);

    // To handle reverse pair if you’re not enforcing user1 < user2
    Conversation findByUser1_IdAndUser2_Id(Long user1Id, Long user2Id);
    Conversation findByUser2_IdAndUser1_Id(Long user1Id, Long user2Id);
}
