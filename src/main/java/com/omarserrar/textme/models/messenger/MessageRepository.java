package com.omarserrar.textme.models.messenger;

import com.omarserrar.textme.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Transactional
public interface MessageRepository extends JpaRepository<Message, Long> {

}
