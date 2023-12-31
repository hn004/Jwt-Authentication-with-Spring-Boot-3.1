package com.security.service;

import com.security.entities.User;
import com.security.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private List<User> store=new ArrayList<>();

//    public UserService(){
//        store.add(new User(UUID.randomUUID().toString(),"Soham K","soham@gamil.com"));
//        store.add(new User(UUID.randomUUID().toString(),"Gitesh B","gitesh@gamil.com"));
//        store.add(new User(UUID.randomUUID().toString(),"Pradnesh B","pradnesh@gamil.com"));
//        store.add(new User(UUID.randomUUID().toString(),"Harshal H","harshal@gamil.com"));
//    }

    public List<User> getUsers(){
        return userRepo.findAll();
    }

    public User createUser(User user){
        user.setUserId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

}
