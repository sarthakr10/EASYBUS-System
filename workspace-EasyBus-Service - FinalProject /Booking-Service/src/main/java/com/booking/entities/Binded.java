package com.booking.entities;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Binded {
	Booking booking;
	Bus bus;
}
