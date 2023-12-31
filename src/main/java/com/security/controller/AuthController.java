package com.security.controller;

import com.security.entities.User;
import com.security.model.JwtRequest;
import com.security.model.JwtResponse;
import com.security.repositories.UserRepo;
import com.security.sec.JwtHelper;
import com.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private JwtHelper helper;
    @Autowired
    private UserService service;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> Login(@RequestBody JwtRequest request){
        this.doAuthenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails= userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);

        JwtResponse response = new JwtResponse(token, userDetails.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    public void doAuthenticate(String email, String password){
        UsernamePasswordAuthenticationToken authentication= new UsernamePasswordAuthenticationToken(email, password);
        try{
            manager.authenticate(authentication);
        }catch(BadCredentialsException e){
            throw new BadCredentialsException("invalid credentials");
        }
    }

    @PostMapping("/create")
    public User createUser(@RequestBody User user){
        return service.createUser(user);
    }



}
