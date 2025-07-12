package com.booking.entities;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int bookingId;
	private int busId;
	private int noOfBookings;
	@Column(name = "Sleeper")
	private int typeSleeperCapacity;
	@Column(name = "Sitting")
	private int typeSittingCapacity;	
	private LocalDate bookingDate;
	private double sititngCost;
	private double sleeperCost;
	private double totalBookingCost;
	private double refund;
	private String bookingEmail;
	@Transient
	List<Passenger> passengerList;
	private String userName;
}
