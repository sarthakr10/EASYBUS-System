package com.easybus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import com.easybus.entities.Bus;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, Integer> {

	public List<Bus> findByBusSourceAndBusDestination(String busSource, String busDestination);

	// public List<Bus> findByBusSourceAndBusDestination(String busSource, String
	// busDestination);
	public List<Bus> findByBusDurationIsLessThanEqual(double busDuration);
	
    boolean existsByBusName(String busName);

	public List<Bus> findByBusSourceAndBusDestinationAndBusDate(String busSource, String busDestination,
			LocalDate busDate);

	public List<Bus> findByBusDate(LocalDate busDate);

}
