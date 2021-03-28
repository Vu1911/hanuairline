package com.se2.hanuairline.service.user;

import com.se2.hanuairline.model.user.User;
import com.se2.hanuairline.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService  {


@Autowired
    UserRepository userRepository ;
    public List<User> getAllAccount(){
        List<User> userList = userRepository.findAll();
    return userList;
    }
}
