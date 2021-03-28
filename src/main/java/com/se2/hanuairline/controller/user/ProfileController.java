package com.se2.hanuairline.controller.user;


import com.se2.hanuairline.exception.InvalidInputValueException;
import com.se2.hanuairline.model.user.Profile;
import com.se2.hanuairline.payload.user.ProfilePayload;
import com.se2.hanuairline.service.user.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// CRUD By userId
@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    ProfileService profileService;

    // finished // no need
//    @PostMapping("/new")
//    public ResponseEntity<?> createNewProfle(@RequestBody ProfilePayload profilePayload){
//       ResponseEntity<?> responseEntity;
//        Profile profile = null;
//        try {
//
//          profile   = profileService.createNewProfile(profilePayload);
//            responseEntity = new ResponseEntity<>(profile, HttpStatus.OK);
//        } catch (InvalidInputValueException e) {
//            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
//        }
//        return responseEntity;
//
//
//    }

    // finished // checked API
    @GetMapping("/allRecords")
    public ResponseEntity<?> getAllProfile(){
        ResponseEntity<?> responseEntity;

       List<Profile> profileList= profileService.getAllRecords();
       responseEntity = new ResponseEntity<>(profileList,HttpStatus.OK);

       return responseEntity;
    }


    // get one by user id // finished // checked API
    @GetMapping("/get-one/{userId}")
    public ResponseEntity<?> getProfileById(@PathVariable("userId") Long userId){
        System.out.println("In here");
       ResponseEntity<?> responseEntity;
        Profile result = null;
        try {
          result  =   profileService.getRecordByUserId(userId);
          responseEntity = new ResponseEntity<>(result,HttpStatus.OK);
        } catch (InvalidInputValueException e) {
            responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.OK);
        }
        return responseEntity;

    }

    // update one record by user id // checked API
    @PutMapping("/update-one/{userId}")
    public ResponseEntity<?>updateOneProfileById(@PathVariable("userId") Long userId,@RequestBody ProfilePayload profilePayload){
       ResponseEntity<?> responseEntity;
       Profile result = null;
        try {
             result=   profileService.updateRecordByUserId(userId,profilePayload);
             responseEntity = new ResponseEntity<>(result,HttpStatus.OK);
        } catch (InvalidInputValueException e) {
            responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
        return responseEntity;
    }

    // deleteOneByUserId // finished // checked API
    @DeleteMapping("/delete-one/{userId}")
    public ResponseEntity<?> deleteOneByUserId(@PathVariable("userId")Long userId){
        ResponseEntity<?> responseEntity ;
        try {
         Profile profile=    profileService.deleteOneByUserId(userId);
         responseEntity = new ResponseEntity<>(profile,HttpStatus.OK);
        } catch (InvalidInputValueException e) {
            responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.OK);
        }
        return responseEntity;

    }



}
