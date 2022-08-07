package com.smartContactManager.service;

import com.smartContactManager.entity.Contact;
import com.smartContactManager.entity.User;
import com.smartContactManager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User addUser(User user){
        return this.userRepository.save(user);
    }
}
