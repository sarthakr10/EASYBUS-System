package com.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.booking.entities.Booking;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer>{
	public List<Booking> findByBusId(int busId);
	public List<Booking> findByBookingEmail(String bookingEmail);
}
