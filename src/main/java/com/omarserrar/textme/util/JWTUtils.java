package com.omarserrar.textme.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.omarserrar.textme.user.User;

import java.util.Date;

public class JWTUtils {
    public static String getUserJWT(User user){
        return JWT.create()
                .withClaim("username",user.getUsername())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*10))
                .sign(Algorithm.HMAC256("secret"));
    }

    public static String getUserNameFromJWT(String jwt){
        return JWT.decode(jwt).getClaim("username").asString();
    }
}
