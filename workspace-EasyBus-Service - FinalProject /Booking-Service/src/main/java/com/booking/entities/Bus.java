package com.booking.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bus {
	private int busId;
	private String busName;
	private String busType;
	private int sittingCapacity;
	private int typeSleeperCapacity;
	private int typeSittingCapacity;
	private String busSource;
	private String busDestination;
	private double busDuration;
	private LocalTime departureFromSource;
	private LocalTime arrivalOnDestination;
	private double priceSitting;
	private double priceSleeper;
	private LocalDate busDate;
}
