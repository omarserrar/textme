package com.omarserrar.textme.models.messenger;

import com.omarserrar.textme.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Transactional
public interface MessengerRepository extends JpaRepository<Conversation, Long> {
    @Query(value ="SELECT * FROM conversation WHERE user1_id=:userId OR user2_id=:userId", nativeQuery = true)
    public List<Conversation> getUserConversation(Long userId);

    @Query(value ="SELECT * FROM conversation WHERE (user1_id=:user1 AND user2_id=:user2) OR (user1_id=:user2 AND user2_id=:user1) LIMIT 1", nativeQuery = true)
    public Conversation getConversationFromUsers(Long user1, Long user2);


}
