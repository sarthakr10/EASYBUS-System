package com.passenger.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passenger {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int passengerId;
	private int bookingId;
	private String passengerName;
	private long passengerPhoneNumber;
	private String passengerGender;
	private int passengerAge;
	private String passengerPrefrence;
}
