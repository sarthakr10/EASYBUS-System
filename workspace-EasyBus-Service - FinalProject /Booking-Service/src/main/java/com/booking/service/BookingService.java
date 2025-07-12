package com.booking.service;

import java.util.List;


import com.booking.entities.Binded;
import com.booking.entities.Booking;

public interface BookingService {

	public Booking addBooking(Booking booking);

	public Booking getBookingById(int id);

	public List<Booking> getBookingsByBusId(int busId);

	public List<Booking> getAllBookings();

	public Booking deleteBooking(int id);

	public String cancelBookingPartiallyByPassengerId(int passengerId);

	public Booking refundCalculator(Booking booking, String preference);

	public List<Binded> getBookingsByBookingEmail(String bookingEmail);

}