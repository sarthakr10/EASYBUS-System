package com.easybus.globalexceptionhandler;

import java.util.HashMap;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.easybus.customexception.BusNameAllReadyExist;
import com.easybus.customexception.BusNotFoundException;
 
@RestControllerAdvice
public class GlobalExceptionHandler {
 
    @ExceptionHandler(BusNotFoundException.class)
    public ResponseEntity<String> handleBusNotFoundException(BusNotFoundException ex) {
       return ResponseEntity.badRequest().body(ex.getMessage());
    }
 
    @ExceptionHandler(BusNameAllReadyExist.class)
    public ResponseEntity<String> handleBusNotFoundException(BusNameAllReadyExist ex) {
       return ResponseEntity.badRequest().body(ex.getMessage());
    }
    // Add more exception handlers for other exceptions if needed

    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
    }
  
    
}
