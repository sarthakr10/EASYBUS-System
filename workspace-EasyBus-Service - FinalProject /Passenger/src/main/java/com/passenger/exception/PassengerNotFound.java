package com.passenger.exception;

public class PassengerNotFound extends RuntimeException {
	public PassengerNotFound(String message) {
		super(message);
	}
}
