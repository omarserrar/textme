package com.omarserrar.textme.services;

import com.omarserrar.textme.models.user.SessionRepository;
import com.omarserrar.textme.models.user.User;
import com.omarserrar.textme.models.user.UserSessions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;


}
