package com.omarserrar.textme.configurations.filters;

import com.omarserrar.textme.models.user.SessionRepository;
import com.omarserrar.textme.models.user.User;
import com.omarserrar.textme.models.user.UserRepository;
import com.omarserrar.textme.models.user.UserSessions;
import com.omarserrar.textme.util.ExpiredJWTException;
import com.omarserrar.textme.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    UserRepository userRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        String token = null;
        Long uid = null;
        String type = null;
        if(null != authorization && authorization.startsWith("Bearer ")) {
            ////System.out.printf("here bearer ");
            token = authorization.substring(7);
            //System.out.printf(token);
            uid = JWTUtils.getUserId(token);
            type = JWTUtils.getTokenType(token);
        }
        if(type != null && type.equals("AUTH") && uid != null && JWTUtils.isExpired(token)){
            User userDetails = userRepository.findById(uid).orElseThrow();
            UserSessions us = new UserSessions(token);
            userDetails.getSessions().remove(us);
            userRepository.save(userDetails);
        }
        else if(type != null && type.equals("AUTH") && uid != null && SecurityContextHolder.getContext().getAuthentication()==null){
            //System.out.printf("User Found");
            User userDetails = userRepository.findById(uid).orElseThrow();
            UserSessions us = new UserSessions(token);
            if(!userDetails.getSessions().contains(us)){
               /* sessionRepository.save(us);
                userDetails.getSessions().add(us);
                userRepository.save(userDetails);*/
            }
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //System.out.printf("Auth");
        }
        else{
            //System.out.printf("user not found");
        }
        filterChain.doFilter(request, response);
    }
}
