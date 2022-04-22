package com.omarserrar.textme.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.omarserrar.textme.models.user.User;

import java.util.Date;

public class JWTUtils {
    public static String getUserJWT(User user){
        return JWT.create()
                .withClaim("username",user.getUsername())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*24*100))
                .sign(Algorithm.HMAC256("secret"));
    }

    public static String getUserNameFromJWT(String jwt) {
        DecodedJWT jwtDecoded = JWT.decode(jwt);
        return jwtDecoded.getClaim("username").asString();
    }

    public static boolean validateTempToken(User u, String jwt) throws ExpiredJWTException {
        DecodedJWT jwtDecoded = JWT.decode(jwt);
        if(isExpired(jwtDecoded)) throw new ExpiredJWTException();
        return u.getId() == Long.parseLong(jwtDecoded.getClaim("id").asString());
    }
    public static boolean isExpired(String jwt){
        return isExpired(JWT.decode(jwt));
    }
    public static boolean isExpired(DecodedJWT jwt){
        long jwtExpiresAt = jwt.getExpiresAt().getTime()/1000;
        long difference = jwtExpiresAt - (System.currentTimeMillis()/1000);
        if(difference < 0){
            return true;
        }
        return false;
    }
    public static String getTemporaryFileReadKey(User u){
        return JWT.create()
                .withClaim("uid",u.getId())
                .withClaim("type", "IMG_KEY")
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60))
                .sign(Algorithm.HMAC256("secret"));
    }
}
