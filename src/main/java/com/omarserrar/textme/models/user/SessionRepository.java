package com.omarserrar.textme.models.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<UserSessions, String> {

}
