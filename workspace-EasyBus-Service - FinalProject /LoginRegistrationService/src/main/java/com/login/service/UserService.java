package com.login.service;

import com.login.dto.AuthDto;
import com.login.exception.InvalidCredentialException;
import com.login.exception.UserAlreadyPresentException;
import com.login.model.User;

public interface UserService {

	public Boolean registerUser(User user) throws UserAlreadyPresentException;

	
	
	public String login(AuthDto loginUser) throws InvalidCredentialException;
}
