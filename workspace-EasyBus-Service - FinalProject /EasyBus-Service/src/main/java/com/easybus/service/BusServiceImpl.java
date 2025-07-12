package com.easybus.service;

import java.time.Duration;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easybus.customexception.BusNameAllReadyExist;
import com.easybus.customexception.BusNotFoundException;
import com.easybus.entities.Booking;
import com.easybus.entities.Bus;
import com.easybus.feign.EasyBusFeign;
import com.easybus.repository.BusRepository;

@Service
public class BusServiceImpl implements BusService {

	private static final Logger logger = LoggerFactory.getLogger(BusServiceImpl.class);

	@Autowired
	private BusRepository busRepository;

	@Autowired
	private EasyBusFeign easyBusFeign;

	@Override
	public Bus addBus(Bus bus) {
		List<Bus> allBusList=busRepository.findAll();
		
		if (busRepository.existsByBusName(bus.getBusName())) {
            throw new BusNameAllReadyExist("Bus with name '" + bus.getBusName() + "' already exists.");
        }
		
		for(Bus b:allBusList) {
			if(b.getBusId()==bus.getBusId() && b.getBusName().equalsIgnoreCase(bus.getBusName())) {
				throw  new BusNotFoundException("Bus cannot be added as with same bus ID and Name");
			}
		}
		logger.info("Attempting to add bus: {}", bus);
		if ((bus.getTypeSittingCapacity() + bus.getTypeSleeperCapacity()) != bus.getSittingCapacity()) {
			logger.error("Total seats in bus do not match the sum of sitting and sleeper capacities.");
			throw new BusNotFoundException("Total seats in bus is not equal to sitting and sleeper");
		}
		if (LocalDate.now().isAfter(bus.getBusDate())) {
			throw new BusNotFoundException("Bus cannot be added for old dates");
		}
		// Set hours by the time difference in between departure and arrival
		double dur=(Duration.between(bus.getDepartureFromSource(), bus.getArrivalOnDestination()).toMinutes()/60.0);
		dur=dur<0?24+dur:dur;
		bus.setBusDuration(dur);
		///
		Bus savedBus = busRepository.save(bus);
		logger.info("Bus added successfully: {}", savedBus);
		return savedBus;
	}
	
	@Override
	public Bus updateBus(Bus bus) {
		logger.info("Attempting to add bus: {}", bus);
		if ((bus.getTypeSittingCapacity() + bus.getTypeSleeperCapacity()) != bus.getSittingCapacity()) {
			logger.error("Total seats in bus do not match the sum of sitting and sleeper capacities.");
			throw new BusNotFoundException("Total seats in bus is not equal to sitting and sleeper");
		}
		if (LocalDate.now().isAfter(bus.getBusDate())) {
			throw new BusNotFoundException("Bus cannot be added for old dates");
		}
		// Set hours by the time difference in between departure and arrival
		double dur=(Duration.between(bus.getDepartureFromSource(), bus.getArrivalOnDestination()).toMinutes()/60.0);
		dur=dur<0?24+dur:dur;
		bus.setBusDuration(dur);
		///
		Bus savedBus = busRepository.save(bus);
		logger.info("Bus added successfully: {}", savedBus);
		return savedBus;
	}
	
	@Override
	public Bus updateBusForAdmin(Bus bus) {
		logger.info("Attempting to add bus: {}", bus);
		
		if (LocalDate.now().isAfter(bus.getBusDate())) {
			throw new BusNotFoundException("Bus cannot be added for old dates");
		}
		// Set hours by the time difference in between departure and arrival
		double dur=(Duration.between(bus.getDepartureFromSource(), bus.getArrivalOnDestination()).toMinutes()/60.0);
		dur=dur<0?24+dur:dur;
		bus.setBusDuration(dur);
		///
		Bus savedBus = busRepository.save(bus);
		logger.info("Bus added successfully: {}", savedBus);
		return savedBus;
	}

	@Override
	public List<Bus> getAllBus() {
		logger.info("Fetching all buses");
		
		List<Bus> allBus = null;
		try {
			allBus = busRepository.findAll();
			if (allBus.isEmpty()) {
				logger.warn("No buses found");
				// Return 404 if list is empty, or
				throw new BusNotFoundException("No buses added");
				// ResponseEntity.ok() if you prefer
			}
			logger.info("Buses fetched successfully");
			return allBus;
		} catch (Exception e) {
			logger.error("Error fetching buses: {}", e.getMessage());
			throw new BusNotFoundException("Error fetching buses: " + e.getMessage());
		}
	}

	@Override
	public Bus findBusById(int id) {
		logger.info("Fetching bus with ID: {}", id);
		Bus bus = busRepository.findById(id).orElseThrow(() -> {
			logger.error("Bus not found with id: {}", id);
			return new BusNotFoundException("Bus not found with id: " + id);
		});
		if (LocalDate.now().isAfter(bus.getBusDate())) {
			throw new BusNotFoundException("Bus cannot be shown for old dates");
		}
		Bus dynamicBusPrice = dynamicBusPrice(bus);
		logger.info("Bus fetched successfully: {}", dynamicBusPrice);
		return dynamicBusPrice; 
	}

	@Override
	public Bus findBusByIdForAdmin(int id) {
		logger.info("Fetching bus for admin with ID: {}", id);
		Bus bus = busRepository.findById(id).orElseThrow(() -> {
			logger.error("Bus not found with id: {}", id);
			return new BusNotFoundException("Bus not found with id: " + id);
		});
		logger.info("Bus fetched successfully for admin: {}", bus);
		return bus;
	}

	@Override
	public List<Bus> findBusBySourceAndDestination(String busSource, String busDestination) {
		logger.info("Fetching buses from {} to {}", busSource, busDestination);
		List<Bus> buses = busRepository.findByBusSourceAndBusDestination(busSource, busDestination);
		if (buses.isEmpty()) {
	        logger.error("No buses found from {} to {}", busSource, busDestination);
	        throw new BusNotFoundException("No buses found from " + busSource + " to " + busDestination);
	    }
		
		List<Bus> dynamicBusPrice = dynamicBusPrice(buses);
		logger.info("Buses fetched successfully: {}", dynamicBusPrice);
		return dynamicBusPrice;
	}

	@Override
	public List<Bus> findBySourceAndDestinationAndDate(String busSource, String busDestination,
			LocalDate busDate) {
		if (LocalDate.now().isAfter(busDate)) {
			throw new BusNotFoundException("Bus cannot be added for old dates");
		}
		logger.info("Fetching buses from {} to {} on date {}", busSource, busDestination, busDate);
		List<Bus> blList = busRepository.findByBusSourceAndBusDestinationAndBusDate(busSource, busDestination, busDate);
		List<Bus> currentBusList=new ArrayList<>();
		
		for(Bus bus: blList) {
			if(LocalDate.now().isBefore(bus.getBusDate())) {
				currentBusList.add(bus);
			}
			else {	
			if(LocalDate.now().isEqual(bus.getBusDate()) && LocalTime.now().isBefore(bus.getDepartureFromSource())) {
				currentBusList.add(bus);
			}
			}
		}
		
		List<Bus> dynamicBusPrice = dynamicBusPrice(currentBusList);
		if (dynamicBusPrice.isEmpty()) {
	        logger.warn("No buses available from {} to {} on {}", busSource, busDestination, busDate);
	        throw new BusNotFoundException("No buses available from " + busSource + " to " + busDestination + " on " + busDate);
	    }
		logger.info("Buses fetched successfully: {}", dynamicBusPrice);
		return dynamicBusPrice;
	}

	@Override
	public List<Bus> findBusByDuration(double busDuration) {
		logger.info("Fetching buses with duration less than or equal to {}", busDuration);
		List<Bus> buses = busRepository.findByBusDurationIsLessThanEqual(busDuration);
		if (buses.isEmpty()) {
	        logger.error("No buses found with less than or equal to {} Hours duration", busDuration);
	        throw new BusNotFoundException("No buses found with less than or equal to " + busDuration + " Hours Duration");
		}
	    
		List<Bus> dynamicBusPrice = dynamicBusPrice(buses);
		logger.info("Buses fetched successfully: {}", dynamicBusPrice);
		return dynamicBusPrice;
	}

	@Override
	public Bus updateBusById(int id) {
		logger.info("Updating bus with ID: {}", id);
		Bus bus = busRepository.findById(id).orElseThrow(() -> {
			logger.error("Bus not found with id: {}", id);
			return new BusNotFoundException("Bus not found with id: " + id);
		});

		List<Booking> bookingList = easyBusFeign.getBookingsByBusId(id);
		if(bookingList.isEmpty()) {
			throw new BusNotFoundException("Bus not found with id: IDK " + id);
		}
		int countOfAvaiSleeper = 0;
		int countOfAvailSitting = 0;
		for (Booking booking : bookingList) {
			countOfAvaiSleeper += booking.getTypeSleeperCapacity();
			countOfAvailSitting += booking.getTypeSittingCapacity();
		}
		bus.setTypeSleeperCapacity(bus.getTypeSleeperCapacity() - countOfAvaiSleeper);
		bus.setTypeSittingCapacity(bus.getTypeSittingCapacity() - countOfAvailSitting);
		logger.debug("Updated bus: {}", bus);
		if (LocalDate.now().isAfter(bus.getBusDate())) {
			throw new BusNotFoundException("Bus cannot be added for old dates");
		}
		///
		Duration duration = Duration.between(bus.getDepartureFromSource(), bus.getArrivalOnDestination());
		double hours = duration.toMinutes() / 60.0;
		bus.setBusDuration(hours);

		Bus updatedBus = busRepository.save(bus);
		logger.info("Bus updated successfully: {}", updatedBus);
		return updatedBus;
	}

	@Override
	public Bus deleteBusById(int id) {
		logger.info("Deleting bus with ID: {}", id);
		Bus bus = busRepository.findById(id).orElse(null);
		if (bus == null) {
			logger.error("Bus not found with id: {}", id);
			throw new BusNotFoundException("Bus not found with id: " + id);
		}
		busRepository.delete(bus);
		logger.info("Bus deleted successfully: {}", bus);
		return bus;
	}

	@Override
	public Bus updateBusWhileCancellation(int busId, String preference) {
		logger.info("Updating bus cancellation details for bus ID: {} with preference: {}", busId, preference);
		Bus bus = busRepository.findById(busId).orElseThrow(() -> {
			logger.error("Bus not found with id: {}", busId);
			return new BusNotFoundException("Bus not found with id: " + busId);
		});
		if (preference.equalsIgnoreCase("Sleeper")) {
			bus.setTypeSleeperCapacity(bus.getTypeSleeperCapacity() + 1);
		} else if (preference.equalsIgnoreCase("Sitting")) {
			bus.setTypeSittingCapacity(bus.getTypeSittingCapacity() + 1);
		}
		busRepository.save(bus);
		logger.info("Bus cancellation details updated successfully: {}", bus);
		return bus;
	}

	@Override
	public Bus dynamicBusPrice(Bus bus) {
		logger.info("Calculating dynamic price for bus: {}", bus);
		long days = ChronoUnit.DAYS.between(LocalDate.now(), bus.getBusDate());
		if (days < 1 || (bus.getTypeSittingCapacity() + bus.getTypeSleeperCapacity()) < 8) {
			bus.setPriceSitting(bus.getPriceSitting() * 1.40);
			bus.setPriceSleeper(bus.getPriceSleeper() * 1.40);
		} else if (days < 3 || (bus.getTypeSittingCapacity() + bus.getTypeSleeperCapacity()) < 12) {
			bus.setPriceSitting(bus.getPriceSitting() * 1.20);
			bus.setPriceSleeper(bus.getPriceSleeper() * 1.20);
		} else if (days < 5 || (bus.getTypeSittingCapacity() + bus.getTypeSleeperCapacity()) < 18) {
			bus.setPriceSitting(bus.getPriceSitting() * 1.1);
			bus.setPriceSleeper(bus.getPriceSleeper() * 1.1);
		}
		logger.info("Dynamic price calculated for bus: {}", bus);
		return bus;
	}

	@Override
	public List<Bus> dynamicBusPrice(List<Bus> busList) {
		logger.info("Calculating dynamic price for bus list");
		List<Bus> updatedBusList = new ArrayList<>();

		for (int i = 0; i < busList.size(); i++) {
			long days = ChronoUnit.DAYS.between(LocalDate.now(), busList.get(i).getBusDate());
			if (days < 1 || (busList.get(i).getTypeSittingCapacity() + busList.get(i).getTypeSleeperCapacity() < 8)) {
				busList.get(i).setPriceSitting(busList.get(i).getPriceSitting() * 1.40);
				busList.get(i).setPriceSleeper(busList.get(i).getPriceSleeper() * 1.40);
			} else if (days < 3
					|| (busList.get(i).getTypeSittingCapacity() + busList.get(i).getTypeSleeperCapacity() < 12)) {
				busList.get(i).setPriceSitting(busList.get(i).getPriceSitting() * 1.20);
				busList.get(i).setPriceSleeper(busList.get(i).getPriceSleeper() * 1.20);
			} else if (days < 5
					|| (busList.get(i).getTypeSittingCapacity() + busList.get(i).getTypeSleeperCapacity() < 18)) {
			busList.get(i).setPriceSitting(busList.get(i).getPriceSitting() * 1.1);
				busList.get(i).setPriceSleeper(busList.get(i).getPriceSleeper() * 1.1);
			}
			updatedBusList.add(busList.get(i));
		}
		logger.info("Dynamic price calculated for bus list");
		return updatedBusList;
	}

	@Override
	public List<String> getAllDestinations() {
		List<Bus> busses= busRepository.findAll();
		if(busses.isEmpty()) {
			logger.error("No buses added");
	        throw new BusNotFoundException("No buses added");
			
		}
		return busses.stream().map(b->b.getBusDestination()).collect(Collectors.toSet()).stream().toList();
		
	}

	@Override
	public  List<String> getAllSources() {
		List<Bus> busses= busRepository.findAll();
		if(busses.isEmpty()) {
			logger.error("No buses added");
	        throw new BusNotFoundException("No buses added");
			
		}
		return busses.stream().map(b->b.getBusSource()).collect(Collectors.toSet()).stream().toList();
	
		
	}
}
