	package com.easybus.entities;
	
	import java.time.LocalDate;
	import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
	import jakarta.persistence.GeneratedValue;
	import jakarta.persistence.GenerationType;
	import jakarta.persistence.Id;
	import jakarta.validation.constraints.Min;
	import jakarta.validation.constraints.NotEmpty;
	import jakarta.validation.constraints.NotNull;
	import jakarta.validation.constraints.Pattern;
	import lombok.AllArgsConstructor;
	import lombok.Data;
	import lombok.NoArgsConstructor;
	
	@Entity
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public class Bus {
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private int busId;
		@NotEmpty(message = "Name cannot be null or empty")
		@Pattern(regexp = "^([A-Z]{3}-[0-9]{3})$", message = "Bus name should be in the format NUE-101")
	    @Column(unique = true)
		private String busName;
		
		@NotEmpty(message = "Specify bus type AC or Non-Ac")
		@Pattern(regexp = "^(AC|Non-Ac|Non-AC|Ac|NON-AC)$", message = "Must be one of: Ac, AC,Non-AC,Non-Ac,NON-AC")
		private String busType;
		
		@NotNull(message = "Sitting Capacity = sleeperCapacity+sittingCapacity")
		private int sittingCapacity;
		
		@NotNull(message = "Sleeper capacity cannot be null or empty")
		private int typeSleeperCapacity;
		
		@NotNull(message = "Sitting capacity cannot be null or empty")
		private int typeSittingCapacity;
		
		@NotEmpty(message = "Source value cannot be null or empty")
		private String busSource;
		
		@NotEmpty(message = "Destination value cannot be null or empty")
		private String busDestination;
		
	//	@Min(value = 1, message = "Duration Time cannot be less than 1" )
		private double busDuration;
		
		private LocalTime departureFromSource;
		
		private LocalTime arrivalOnDestination;
		
		@NotNull(message = "Bus price cannot be zero or null")
		//@Min(value = 1250, message = "Minimum bus price is 1250")
		private double priceSitting;
		
		@NotNull(message = "Bus price cannot be zero or null")
		//@Min(value = 1250, message = "Minimum bus price is 1250")
		private double priceSleeper;
		
		@NotNull(message = "Date cannnot be null")
		private LocalDate busDate;
	}
