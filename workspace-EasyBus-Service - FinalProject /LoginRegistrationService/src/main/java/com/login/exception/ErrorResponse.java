package com.login.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
public class ErrorResponse {
	private final HttpStatus status;
	private final String message;
	
}
