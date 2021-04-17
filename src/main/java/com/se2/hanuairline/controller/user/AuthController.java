package com.se2.hanuairline.controller.user;

import com.se2.hanuairline.exception.InvalidInputValueException;
import com.se2.hanuairline.model.user.*;
import com.se2.hanuairline.payload.ApiResponse;
import com.se2.hanuairline.payload.user.JwtAuthenticationResponse;
import com.se2.hanuairline.payload.user.LoginRequest;
import com.se2.hanuairline.payload.user.ProfilePayload;
import com.se2.hanuairline.payload.user.SignUpRequest;
import com.se2.hanuairline.repository.user.RoleRepository;
import com.se2.hanuairline.repository.user.UserRepository;
import com.se2.hanuairline.security.JwtAuthenticationFilter;
import com.se2.hanuairline.security.JwtTokenProvider;
import com.se2.hanuairline.service.user.AuthService;
import com.se2.hanuairline.service.user.ProfileService;
import com.se2.hanuairline.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    AuthService authService;

    @Autowired
    private ProfileService profileService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );


        SecurityContextHolder.getContext().setAuthentication(authentication);
        Optional<User> user = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(), loginRequest.getUsernameOrEmail());

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, user.get()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) throws InvalidInputValueException {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setImageUrl(signUpRequest.getImageUrl());
        user.setProvider(AuthProvider.local);
        user.setStatus(UserStatus.CREATED);

        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        Optional<Role> userRole = roleRepository.findByName(RoleName.ROLE_USER);

        user.setRoles(Collections.singleton(userRole.get()));

        User result = userRepository.save(user);

        ProfilePayload profilePayload = new ProfilePayload(result.getId(),null,null,null);
        // remove invalidInputValueException later
        profileService.createNewProfile(profilePayload);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("api/user/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/getMe")
    public ResponseEntity<?> getMe(HttpServletRequest request){
        String token = jwtAuthenticationFilter.getJwtFromRequest(request);

        if(!tokenProvider.validateToken(token)){
            return new ResponseEntity<>(null, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }

        Long userId = tokenProvider.getUserIdFromJWT(token);

        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()){
            return new ResponseEntity<>(null, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }

        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }
}
