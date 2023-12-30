package com.security.sec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
public class JwtHelper {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;   // valid for 5 hours

    private String secret ="hellllhellhellhellhellhellhellhellhellhellhellhellhellllhellllhellllhelllhellllhelllhellhellhellhellhellhellhellhellhellhellhellhellhell" ;
    
    public String getUsernameFromToken(String token){
        //get the username from the token 
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        System.out.println("expiration date "+getClaimFromToken(token, Claims::getExpiration).toString());
        return getClaimFromToken(token, Claims::getExpiration);
    }

//    This method getClaimFromToken() provides a way to retrieve a specific
//    claim from a JWT by allowing the caller to define
//    the logic for extracting that claim using the claimsResolver function.
    private <T> T getClaimFromToken(String token, Function<Claims, T> calimsResolver) {
        final Claims claims= getAllClaimsFromToken(token);
        return calimsResolver.apply(claims);
    }

//    this getAllClaimsFromToken method encapsulates the process of parsing
//    a JWT, verifying its signature using a secret key, and extracting the
//    claims embedded within the token for further processing or validation
//    in the application.
    private Claims getAllClaimsFromToken(String token) {
        System.out.println("Parsed jwt token: "+Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().toString());
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails){
        Map<String, Objects> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Objects> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims) // Set the claims of the token
                .setSubject(subject) // Set the subject of the token (typically representing the user)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Set the token's issuance time
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY *1000 )) // Set token expiration time
                .signWith(SignatureAlgorithm.HS512, secret) // Sign the JWT using HS512 algorithm and a secret key
                .compact();  // Compact the JWT into a URL-safe string according to JWS Compact Serialization
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}
