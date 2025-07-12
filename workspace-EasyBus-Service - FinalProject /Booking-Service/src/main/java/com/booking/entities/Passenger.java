package com.booking.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passenger {
	private int passengerId;
	private int bookingId;
	private String passengerName;
	private long passengerPhoneNumber;
	private String passengerGender;
	private int passengerAge;
	private String passengerPrefrence;
}
