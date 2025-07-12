package com.booking.controller;

import java.util.List;

import java.util.logging.Logger; // Import the Logger class

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.booking.entities.Binded;
import com.booking.entities.Booking;
import com.booking.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private static final Logger logger = Logger.getLogger(BookingController.class.getName()); // Initialize Logger

    @Autowired
    BookingService bookingService;

    @PostMapping("/add")
    public ResponseEntity<Booking> addBooking(@RequestBody @Valid Booking booking) {
        logger.info("Adding booking: " + booking.toString());
        Booking savedBooking = bookingService.addBooking(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
    }
    @GetMapping("/get/{bookingId}")
    public ResponseEntity<Booking> getBookingById(@PathVariable("bookingId") int bookingId) {
        logger.info("Fetching booking by ID: " + bookingId);
        Booking booking = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/getByBusId/{busId}")
    public ResponseEntity<List<Booking>> getBookingsByBusId(@PathVariable("busId") int busId) {
        logger.info("Fetching bookings by Bus ID: " + busId);
        List<Booking> bookings = bookingService.getBookingsByBusId(busId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Booking>> getAllBookings() {
        logger.info("Fetching all bookings.");
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/delete/{bookingId}")
    public ResponseEntity<Booking> deleteBooking(@PathVariable("bookingId") int bookingId) {
    	Booking deletedBooking = bookingService.deleteBooking(bookingId);
        return ResponseEntity.ok(deletedBooking);
    }

    @DeleteMapping("/cancelBooking/{passengerId}")
    public ResponseEntity<String> cancelBookingPartiallyByPassengerId(@PathVariable("passengerId") int passengerId) {
        logger.info("Canceling booking partially by Passenger ID: " + passengerId);
        String result = bookingService.cancelBookingPartiallyByPassengerId(passengerId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getByBookingEmail/{bookingEmail}")
    public ResponseEntity<List<Binded>> getBookingsByBookingEmail(@PathVariable("bookingEmail") String bookingEmail) {
        logger.info("Fetching bookings by Booking Email: " + bookingEmail);
        List<Binded> bindedList = bookingService.getBookingsByBookingEmail(bookingEmail);
        return ResponseEntity.ok(bindedList);
    }

}
