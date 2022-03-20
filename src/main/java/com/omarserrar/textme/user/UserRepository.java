package com.omarserrar.textme.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value ="SELECT * FROM Users WHERE username=:username LIMIT 1", nativeQuery = true)
    public Optional<User> findUserByUsername(String username);
}
