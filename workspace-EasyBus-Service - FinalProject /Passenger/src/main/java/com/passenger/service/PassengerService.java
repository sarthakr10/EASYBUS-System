package com.passenger.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.passenger.entities.Passenger;
import com.passenger.exception.PassengerNotFound;
import com.passenger.repository.PassengerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PassengerService {

    private static final Logger logger = LoggerFactory.getLogger(PassengerService.class);

    @Autowired
    PassengerRepository passengerRepository;
    List<Passenger> passengerList = new ArrayList<>();

    public List<Passenger> addPassengers(List<Passenger> passenger, int bookingId) {
        logger.info("Adding passengers for booking ID: {}", bookingId);
        for (Passenger p : passenger) {
            p.setBookingId(bookingId);
            passengerRepository.save(p);
            logger.info("Passenger added: {}", p);
        }
        logger.info("Successfully added passengers for booking ID: {}", bookingId);
        return passenger;
    }

    public ResponseEntity<List<Passenger>> getPassengerByBookingId(int bookingId) {
        logger.info("Fetching passengers for booking ID: {}", bookingId);
        List<Passenger> passengersList = passengerRepository.findByBookingId(bookingId);
        if (passengersList.isEmpty()) {
            logger.warn("No passengers found for booking ID: {}", bookingId);
        } else {
            logger.info("Fetched {} passengers for booking ID: {}", passengersList.size(), bookingId);
        }
        return ResponseEntity.ok(passengersList);
    }

    public ResponseEntity<Passenger> deletePassengerById(int passengerId) {
        logger.info("Deleting passenger with ID: {}", passengerId);
        Passenger passenger = passengerRepository.findById(passengerId).orElseThrow(() -> {
            logger.error("Passenger not found with ID: {}", passengerId);
            return new PassengerNotFound("Passenger does not exist by ID: " + passengerId);
        });
        passengerRepository.deleteById(passengerId);
        logger.info("Successfully deleted passenger with ID: {}", passengerId);
        return ResponseEntity.ok(passenger);
    }
}
