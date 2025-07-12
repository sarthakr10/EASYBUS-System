package com.login.controller;
 
import java.util.HashMap;

import java.util.Map;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
 
import com.login.dto.AuthDto;
import com.login.exception.InvalidCredentialException;
import com.login.exception.UserAlreadyPresentException;
import com.login.model.User;
import com.login.security.Jwtutil;
import com.login.service.UserServiceImpl;
 
import jakarta.validation.Valid;
 
@RestController
@Validated
@RequestMapping("/api/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
 
    @Autowired
    private UserServiceImpl userService;
 
    @Autowired
    private Jwtutil jwtutil;
 
    @Autowired
    private BCryptPasswordEncoder encoder;
 
    
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody @Valid User user) throws UserAlreadyPresentException {
    	System.err.println(user);
        logger.info("Attempting to register user: {}", user.getName());
        try {
            if (userService.registerUser(user)) {
                logger.info("User registered successfully: {}", user.getName());
                return generateResponse("Register successful", HttpStatus.CREATED, user, null);
            } else {
                logger.warn("Registration failed for user: {}", user.getName());
                return generateResponse("Registration failure", HttpStatus.BAD_REQUEST, user, null);
            }
        } catch (UserAlreadyPresentException e) {
            logger.error("User already present: {}", user.getName(), e);
            throw e;
        }
    }
 
    
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody @Valid AuthDto loginUser) throws InvalidCredentialException {
        logger.info("Attempting to log in user: {}", loginUser.getEmail());
        try {
            String encodedPassword = encoder.encode(loginUser.getPassword());
            String token = userService.login(loginUser);
            loginUser.setPassword(encodedPassword);
            logger.info("User logged in successfully: {}", loginUser.getEmail());
            return generateResponse("Login successful", HttpStatus.OK, loginUser, token);
        } catch (InvalidCredentialException e) {
           logger.error("Invalid credentials for user: {}", loginUser.getEmail(), e);
            throw e;
        }
    }
 
    @GetMapping("/validate")
    public ResponseEntity<String> validate(@RequestParam("token") String token) {
        logger.info("Validating token");
        jwtutil.validateToken(token);
        logger.info("Token is valid");
        return ResponseEntity.ok("Token is valid");
    }
 
    private ResponseEntity<Object> generateResponse(String message, HttpStatus httpStatus, Object responseObj, String token) {
        Map<String, Object> map = new HashMap<>();
        map.put("Message", message);
        map.put("Status", httpStatus.value());
        map.put("Data", responseObj);
        map.put("Token", token);
 
        return new ResponseEntity<>(map, httpStatus);
    }
}