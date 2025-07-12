package com.login.service;

import java.util.List;

import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.login.dto.AuthDto;
import com.login.exception.InvalidCredentialException;
import com.login.exception.UserAlreadyPresentException;
import com.login.model.User;
import com.login.repository.UserRepository;
import com.login.security.Jwtutil;

@Service
public class UserServiceImpl implements UserService{

	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@Autowired
	Jwtutil jwtutil;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public Boolean  registerUser(User user) throws UserAlreadyPresentException {
		List<User> userList = userRepository.findAll();
		if(userList.isEmpty()) {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				User u = userRepository.save(user);
				return true;
		}
		else {
			String email = user.getEmail();
			boolean flag = false;
			for(User u: userList) {
				if(u.getEmail().equalsIgnoreCase(email)) {
					flag = true;
				}
			}
			if(flag) {
				logger.info("User already exists...");
				throw new UserAlreadyPresentException("Can't add user. Already exits.");
			}
			else {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				User u =userRepository.save(user);
				return true;
			}
		}
	}
	

	@Override
	public String login(AuthDto loginUser) throws InvalidCredentialException {
		//need to apply jwt
 	   String str = null;
 	   try {
	 	   Authentication authentication =  authenticationManager.authenticate(
	 			   new UsernamePasswordAuthenticationToken(
	                        loginUser.getEmail(),
	                        loginUser.getPassword()
	                ));
	 	   if(authentication.isAuthenticated()) {
	 		   User u = userRepository.findByEmail(loginUser.getEmail());
	 		   str = jwtutil.generateToken(u.getName(),u.getRole(),u.getId(),u.getEmail());
	 	   	   return str;
	 	   }else {
	 		  throw new InvalidCredentialException("Invalid user name or password");
	 	   }
 	   }
 	   catch(BadCredentialsException e) {
 		   throw new InvalidCredentialException("Invalid user name or password");
 	   }
	}
	
	
	
	

}
