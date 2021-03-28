package com.se2.hanuairline.service.user;

import com.se2.hanuairline.exception.InvalidInputValueException;
import com.se2.hanuairline.model.PriceByClass;
import com.se2.hanuairline.model.user.Profile;
import com.se2.hanuairline.model.user.User;
import com.se2.hanuairline.payload.user.ProfilePayload;
import com.se2.hanuairline.repository.user.ProfileRepository;
import com.se2.hanuairline.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    UserRepository userRepository;

    public Profile createNewProfile(ProfilePayload profilePayload) throws InvalidInputValueException {

        // naturalId already checked for no duplicate userId

        Profile profile = new Profile();
        profile.setUser(userRepository.findById(profilePayload.getUser_id()).get());
        // intial set to null

//        profile.setPhone_number(profilePayload.getPhoneNumber());
//        profile.setCredit_card_number(profilePayload.getCredit_card_number());
//        profile.setId_card_number(profilePayload.getId_card_number());
        Profile result = profileRepository.save(profile);
        return result;

    }

    public List<Profile> getAllRecords(){
        List<Profile> result = profileRepository.findAll();
        return result;
    }

    public Profile getRecordByUserId(Long id) throws InvalidInputValueException {
        if(!checkExisted(id)){
            throw new InvalidInputValueException("Does not exist profile with user_id : "+id);
        }


        return profileRepository.findById(id).get();
    }
    public Profile updateRecordByUserId(Long id, ProfilePayload profilePayload) throws InvalidInputValueException {
        if(!checkExisted(id)){
            throw new InvalidInputValueException("Does not exist profile with user_id : "+id);
        }
        Profile profile = this.getRecordByUserId(id);
        System.out.println(profile.toString());
        profile.setPhone_number(profilePayload.getPhoneNumber());
        profile.setId_card_number(profilePayload.getId_card_number());
        profile.setCredit_card_number(profilePayload.getCredit_card_number());
//       ! not update user
        Profile result = profileRepository.save(profile);
        return result;

    }

    public Profile deleteOneByUserId(Long id) throws InvalidInputValueException {
        if(!checkExisted(id)){
            throw new InvalidInputValueException("profile with this user_id does not exist : "+id);
        }
        Profile profile = this.getRecordByUserId(id);
        profileRepository.deleteByUser_Id(id);
        return profile;


    }
    private boolean checkExisted(Long userId){
//        Optional<Profile> profile = profileRepository.findById(id);
        Optional<User> userData=   userRepository.findById(userId);
        return userData.isPresent();
    }


}
