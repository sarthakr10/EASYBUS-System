package com.easybus.customexception;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ResponseStatus;



public class BusNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BusNotFoundException(String message) {
		super(message);
	}
}
