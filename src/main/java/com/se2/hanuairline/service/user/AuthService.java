package com.se2.hanuairline.service.user;

import com.se2.hanuairline.exception.InvalidInputValueException;
import com.se2.hanuairline.model.user.*;
import com.se2.hanuairline.payload.user.SignUpRequest;
import com.se2.hanuairline.payload.user.UserPayload;
import com.se2.hanuairline.repository.user.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Service
public class AuthService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserService userService;

    public URI signUp(SignUpRequest signUpRequest) throws InvalidInputValueException {

     UserPayload userPayload = new UserPayload(signUpRequest.getName(),signUpRequest.getUsername(),signUpRequest.getEmail(),signUpRequest.getImageUrl(),signUpRequest.getPassword());

     User user=    userService.createNewUser(userPayload);

     URI location = ServletUriComponentsBuilder
             .fromCurrentContextPath().path("api/user/{username}")
                .buildAndExpand(user.getUsername()).toUri();
     return location;
    }
}
