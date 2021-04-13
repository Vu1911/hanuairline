package com.se2.hanuairline.service.user;

import com.se2.hanuairline.exception.InvalidInputValueException;
import com.se2.hanuairline.model.Ticket;
import com.se2.hanuairline.model.user.Role;
import com.se2.hanuairline.model.user.User;
import com.se2.hanuairline.model.user.UserStatus;
import com.se2.hanuairline.payload.ApiResponse;
import com.se2.hanuairline.payload.user.ProfilePayload;
import com.se2.hanuairline.payload.user.UserPayload;
import com.se2.hanuairline.repository.user.RoleRepository;
import com.se2.hanuairline.repository.user.UserRepository;
import com.se2.hanuairline.util.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    public User createNewUser(UserPayload userPayload) throws InvalidInputValueException {
        if(userRepository.existsByUsername(userPayload.getUsername())) {
            throw new InvalidInputValueException("Username is already taken!");

        }

        if(userRepository.existsByEmail(userPayload.getEmail())) {
            throw new InvalidInputValueException("Email Address already in use!");
        }

        Optional<Role> role = roleRepository.findByName(userPayload.getRole());

        if(!role.isPresent()){
            throw new InvalidInputValueException("No such role");
        }

        User user = new User();
        user.setEmail(userPayload.getEmail());
        user.setUsername(userPayload.getUsername());
        user.setName(userPayload.getName());
        user.setPassword(passwordEncoder.encode(userPayload.getPassword()));
        user.getRoles().clear();
        user.getRoles().add(role.get());
      // attach user profile to user
        User createdUser =  userRepository.save(user);
        // stuck here

        ProfilePayload profilePayload = new ProfilePayload(createdUser.getId(),null,null,null);
      // remove invalidInputValueException later
        profileService.createNewProfile(profilePayload);
        return createdUser;
    }

    public Page<User> findAllUser(String email, int page, int size, String[] sort){
        Pageable pagingSort = PaginationUtils.pagingSort(page, size, sort);
        if(email.equals("_")){
            return userRepository.findAll(pagingSort);
        }
        return userRepository.findByEmailContaining(email, pagingSort);
    }

    public User getUserById(Long id){
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()){
            return user.get();
        } else {
            return null;
        }
    }

    public User updateUser (Long id, UserPayload request){
        Optional<User> userData = userRepository.findById(id);

        if(!userData.isPresent()){
            return null;
        }

        User user = userData.get();

        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        User _user = userRepository.save(user);

        return _user;
    }

    public User deleteUserById(Long id) throws InvalidInputValueException {
        Optional<User> userData = userRepository.findById(id);

        if(!userData.isPresent()){
            throw new InvalidInputValueException("Does not exist record with user id :"+id);
        }

//        User user = userDatar
        userRepository.deleteById(id);

        return userData.get();
    }
}
