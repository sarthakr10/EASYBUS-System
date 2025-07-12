package com.passenger.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.passenger.entities.Passenger;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Integer> {
	List<Passenger> findByBookingId(int bookingId);
}
