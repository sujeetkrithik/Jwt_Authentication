package com.example.demo.helper;

//methods - for generating token
//validate
//isExp
//util class for jwt

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

//private static final long SerialVersionUID= 2550185165626007488L;
public static final long JWT_TOKEN_VALIDITY=5*60*60;

private String secret="java";

//retrieve username from jwt token
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }
    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
     final Claims claims=getAllClaimsFromToken(token);
     return claimsResolver.apply(claims);
    }
//for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token){
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
    //check if the token has expired
     private Boolean isTokenExpired(String token){
        final Date expiration=getExpirationDateFromToken(token);
        return expiration.before(new Date());
     }
     //genarate token for user
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims=new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }
     //while creating the token -------
    //1. define claims of token, like Issuer, Expiration, Subject and the ID
    //2. sign the JWT using HS512 algorithm and secret key
    //3. according to JWS compact Serialization(https://tools.ietf.org/html/draft-ie
    // Compaction of JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject){
    return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
            .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
    //validate token
    public Boolean validatetoken(String token, UserDetails userDetails){
      final String username=getUsernameFromToken(token);
      return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
