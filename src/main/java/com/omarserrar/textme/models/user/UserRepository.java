package com.omarserrar.textme.models.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value ="SELECT * FROM Users WHERE username=:username LIMIT 1", nativeQuery = true)
    public Optional<User> findUserByUsername(String username);

    @Query(value ="SELECT * FROM Users WHERE id <>:id AND id NOT IN (SELECT contacts_id FROM users_contacts WHERE user_id=:id)", nativeQuery = true)
    public List<User> discoverUsers(Long id);
}
