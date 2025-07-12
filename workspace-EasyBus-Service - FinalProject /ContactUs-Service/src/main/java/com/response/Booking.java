package com.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
	
	private int bookingId;
	private int busId;
	private int noOfBookings; 
	private int typeSleeperCapacity;
	private int typeSittingCapacity;
	private LocalDate bookingDate;
	private double sititngCost;
	private double sleeperCost;
	private double totalBookingCost;
	private String bookingEmail;
}
