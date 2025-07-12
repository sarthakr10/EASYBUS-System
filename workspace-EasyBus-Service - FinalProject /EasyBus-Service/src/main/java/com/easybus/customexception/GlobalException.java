package com.easybus.customexception;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {
	
	
	@ExceptionHandler(BusNotFoundException.class)
	public ResponseEntity<ErrorDto> globalExceptionHandler(BusNotFoundException exception){
		ErrorDto dto=new ErrorDto();
		dto.setMessage(exception.getMessage());
		dto.setStatus(HttpStatus.NOT_FOUND);
	return new ResponseEntity<ErrorDto>(dto,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(BusNameAllReadyExist.class)
	public ResponseEntity<ErrorDto> globalExceptionHandler(BusNameAllReadyExist exception){
		ErrorDto dto=new ErrorDto();
		dto.setMessage(exception.getMessage());
		dto.setStatus(HttpStatus.FOUND);
	return new ResponseEntity<ErrorDto>(dto,HttpStatus.FOUND);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDto> Errors(Exception exception){
		ErrorDto dto=new ErrorDto();
		dto.setMessage("Something went wrong!!!");
		dto.setStatus(HttpStatus.NOT_FOUND);
		return new ResponseEntity<ErrorDto>(dto,HttpStatus.NOT_FOUND);
	}
	
}
