package com.se2.hanuairline.controller.user;

import com.se2.hanuairline.exception.InvalidInputValueException;
import com.se2.hanuairline.model.user.User;
import com.se2.hanuairline.payload.user.UpdateUserRequest;
import com.se2.hanuairline.payload.user.UserPayload;
import com.se2.hanuairline.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;

    // checked API
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @Secured("ROLE_ADMIN")
    @PostMapping("/admin/create")
    public ResponseEntity<?> createNewUser(@RequestBody UserPayload userPayload){
        ResponseEntity<?> responseEntity ;

        try {
            User result = userService.createNewUser(userPayload);
            responseEntity = new ResponseEntity<>(result,HttpStatus.OK);
        } catch (InvalidInputValueException e) {
            responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.OK);
        }

        return responseEntity;

    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/getAll")
    public ResponseEntity<?> getAllUsers (@RequestParam(required = false, defaultValue = "_") String email,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "id,desc") String[] sort){
        try {
            Page<User> usersData = userService.findAllUser(email, page, size, sort);

            return new ResponseEntity<>(usersData, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/getById/{id}")
    public ResponseEntity<?> getUserByID(@PathVariable Long id){
        User user = userService.getUserById(id);

        if(user == null){
            return new ResponseEntity<>("Wrong id or logic error", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @Secured("ROLE_ADMIN")
    @PutMapping("/admin/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) throws InvalidInputValueException {
        User user = userService.updateUser(id, request);

        if(user == null){
            return new ResponseEntity<>("Wrong user id", HttpStatus.NOT_MODIFIED);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @Secured("ROLE_ADMIN")
    // checked API // finished
    @DeleteMapping("/admin/delete-one/{id}")
    public ResponseEntity<?> deleteOneUser(@PathVariable Long id){
        ResponseEntity<?> responseEntity;
        try {
          User result = userService.deleteUserById(id);
          responseEntity = new ResponseEntity<>(result,HttpStatus.OK);
        } catch (InvalidInputValueException e) {
            responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.OK);
        }
        catch(Exception e){
            responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.OK);

        }
        return responseEntity;
    }

    // update me
    // delete me


}

