package com.se2.hanuairline.service.user;

import com.se2.hanuairline.exception.InvalidInputValueException;
import com.se2.hanuairline.model.user.User;
import com.se2.hanuairline.model.user.UserStatus;
import com.se2.hanuairline.payload.user.ProfilePayload;
import com.se2.hanuairline.payload.user.UserPayload;
import com.se2.hanuairline.repository.user.UserRepository;
import com.se2.hanuairline.util.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileService profileService;

    public User createNewUser(UserPayload userPayload) throws InvalidInputValueException {
        User user = new User();
        user.setEmail(userPayload.getEmail());
        user.setUsername(userPayload.getUsername());
        user.setName(userPayload.getName());
      // attach user profile to user
        User createdUser =  userRepository.save(user);
        // stuck here
        ProfilePayload profilePayload = new ProfilePayload(null,user.getId(),null,null,null);
      // remove invalidInputValueException later
        profileService.createNewProfile(profilePayload);
        return createdUser;
    }

    public Page<User> findAllUser(String username, String name, String email, Long id, int page, int size, String[] sort){
        Pageable pagingSort = PaginationUtils.pagingSort(page, size, sort);

        if(id != null){
            return userRepository.findById(id, pagingSort);
        }

        return userRepository.findByEmailContainingAndUsernameContainingAndNameContaining(email, username, name, pagingSort);
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
