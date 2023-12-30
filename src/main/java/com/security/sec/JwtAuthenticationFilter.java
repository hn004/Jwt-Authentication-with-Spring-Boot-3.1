package com.security.sec;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    //actual request filtering and authentication logic take place
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestHeader = request.getHeader("Authorization"); //Retrieves the value of the "Authorization" header from the incoming HTTP request.
        System.out.println("req header" + requestHeader);
        String username=null;
        String token=null;

        if(requestHeader != null && requestHeader.startsWith("Bearer") ){
            token = requestHeader.substring(7); // Extracts the JWT token by removing the "Bearer " prefix.
            try{
                username = this.jwtHelper.getUsernameFromToken(token);  // retrieve the username from the JWT token.
            }
            // Handle different exceptions related to JWT token processing
            catch (IllegalArgumentException e){
                System.out.println("Illegal argument while fetching the username");
                e.printStackTrace();
            }
            catch(ExpiredJwtException e){
                System.out.println("Given token is expired");
                e.printStackTrace();
            }
            catch(MalformedJwtException e){
                System.out.println("Token modified.... Invalid Token");
                e.printStackTrace();
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }else{  //If the header is invalid or doesn’t start with "Bearer", logs accordingly.
            System.out.println("Invalid Header value");
        }

        //SecurityContextHolder - It holds the security-related information for a user, such as their authentication details and granted authorities (roles or permissions).
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);

            if(validateToken){
                //set authentication

                //Creates an authentication token (UsernamePasswordAuthenticationToken) using user details and sets it in the security context.
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //Once a user is authenticated, you can set their Authentication object in
                // the SecurityContext using SecurityContextHolder.getContext().setAuthentication(authentication).
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }else{
                System.out.println("User validation failed");
            }

        }

        filterChain.doFilter(request, response);

    }
}
