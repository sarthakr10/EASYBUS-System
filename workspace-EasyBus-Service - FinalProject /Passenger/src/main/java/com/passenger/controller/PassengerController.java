package com.passenger.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.passenger.entities.Passenger;
import com.passenger.service.PassengerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/passenger")
public class PassengerController {

    private static final Logger logger = LoggerFactory.getLogger(PassengerController.class);

    @Autowired
    PassengerService passengerService;

    @PostMapping("/add/{bookingId}")
    public List<Passenger> addPassengers(@RequestBody List<Passenger> passenger, @PathVariable("bookingId") int bookingId) {
        logger.info("Received request to add passengers for booking ID: {}", bookingId);
        return passengerService.addPassengers(passenger, bookingId);
    }

    @GetMapping("/getByBookingId/{bookingId}")
    public ResponseEntity<List<Passenger>> getPassengerByBookingId(@PathVariable("bookingId") int bookingId) {
        logger.info("Received request to fetch passengers for booking ID: {}", bookingId);
        return passengerService.getPassengerByBookingId(bookingId);
    }

    @DeleteMapping("/deleteById/{passengerId}")
    public ResponseEntity<Passenger> deletePassengerById(@PathVariable("passengerId") int passengerId) {
        logger.info("Received request to delete passenger with ID: {}", passengerId);
        return passengerService.deletePassengerById(passengerId);
    }
} 
