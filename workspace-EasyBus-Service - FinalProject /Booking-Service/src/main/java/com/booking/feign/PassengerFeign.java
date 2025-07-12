package com.booking.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.booking.entities.Passenger;


@FeignClient(name="PASSENGER/passenger")
public interface PassengerFeign {
	@PostMapping("/add/{bookingId}")
	public List<Passenger> addPassengers(@RequestBody List<Passenger> passenger,@PathVariable("bookingId") int bookingId);
	
	@GetMapping("/getByBookingId/{bookingId}")
	public List<Passenger> getPassengerByBookingId(@PathVariable("bookingId") int bookingId);
	
	@DeleteMapping("deleteById/{passengerId}")
	public ResponseEntity<Passenger> deletePassengerById(@PathVariable("passengerId") int passengerId); 
}
