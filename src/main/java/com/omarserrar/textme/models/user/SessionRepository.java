package com.omarserrar.textme.models.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<UserSessions, String> {
}
