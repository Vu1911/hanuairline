package com.se2.hanuairline.controller.user;

import com.se2.hanuairline.model.user.User;
import com.se2.hanuairline.repository.user.UserRepository;
import com.se2.hanuairline.service.user.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(){
     List<User> userList= accountService.getAllAccount();
     return new ResponseEntity<>(userList, HttpStatus.OK);

//        ResponseEntity<List<User>,> responseEntity =
    }
}
