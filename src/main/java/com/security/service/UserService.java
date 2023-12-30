package com.security.service;

import com.security.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private List<User> store=new ArrayList<>();

    public UserService(){
        store.add(new User(UUID.randomUUID().toString(),"Soham K","soham@gamil.com"));
        store.add(new User(UUID.randomUUID().toString(),"Gitesh B","gitesh@gamil.com"));
        store.add(new User(UUID.randomUUID().toString(),"Pradnesh B","pradnesh@gamil.com"));
        store.add(new User(UUID.randomUUID().toString(),"Harshal H","harshal@gamil.com"));
    }

    public List<User> getUsers(){
        return this.store;
    }

}
