package com.booking.service;

import java.time.LocalDate;



import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger; // Import the Logger class

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.booking.entities.Binded;
import com.booking.entities.Booking;
import com.booking.entities.Bus;
import com.booking.entities.Passenger;
import com.booking.exception.BookingNotFoundException;
import com.booking.feign.BookingFeign;
import com.booking.feign.EmailFeign;
import com.booking.feign.PassengerFeign;
import com.booking.repository.BookingRepository;

@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger logger = Logger.getLogger(BookingServiceImpl.class.getName()); // Initialize Logger

    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private BookingFeign bookingFeign;
    
    @Autowired
    private PassengerFeign passengerFeign;
    
   @Autowired
   private EmailFeign emailFeign;
    
    @Override
	public Booking addBooking(Booking booking) {
        logger.info("Adding booking: " + booking.toString());
    	
    	booking.setNoOfBookings(booking.getTypeSittingCapacity()+booking.getTypeSleeperCapacity());
    	booking.setBookingDate(LocalDate.now());
    	if (booking.getBookingDate() == null) {
            throw new BookingNotFoundException("Booking date cannot be null");
    	}
    	if(booking.getNoOfBookings()!= booking.getPassengerList().size() || 
    			(booking.getTypeSittingCapacity()+booking.getTypeSleeperCapacity())!=booking.getNoOfBookings()) {
    		throw new BookingNotFoundException("Please enter correct number of Passenger deatils or Seat Type");
    	}
    	LocalDate currentDate = LocalDate.now();
        LocalDate bookingDate = booking.getBookingDate();
    	if (bookingDate != null && bookingDate.isBefore(currentDate)) {
            throw new IllegalArgumentException("Booking date must be in the future");
        }
    	Bus bus=bookingFeign.findBusById(booking.getBusId()).getBody();
    	
    	if(booking.getNoOfBookings()>bus.getSittingCapacity()) {
    		throw new BookingNotFoundException("Booking cannot be done since the number of bookings "+booking.getNoOfBookings()+" is greater than the sitting capacity of the bus "+bus.getSittingCapacity());
    	}
    	if(booking.getBookingDate().isAfter(bus.getBusDate())) {
    		throw new BookingNotFoundException("Booking cannot be done of an old date");
    	}
    	
    	booking.setSititngCost(bus.getPriceSitting()*booking.getTypeSittingCapacity());
    	booking.setSleeperCost(bus.getPriceSleeper()*booking.getTypeSleeperCapacity());
    	booking.setTotalBookingCost(booking.getSititngCost()+booking.getSleeperCost());
    	
    	Booking savedBooking = bookingRepository.save(booking);
        bookingFeign.updateBusById(booking.getBusId());
        savedBooking.setPassengerList(passengerFeign.addPassengers(booking.getPassengerList(), booking.getBookingId()));
        emailFeign.bookingMail(savedBooking);        
        logger.info("Booking added successfully: " + savedBooking.toString());
        return savedBooking;
    }
    
    @Override
	public Booking getBookingById(int id) {
        logger.info("Fetching booking by ID: " + id);
        
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + id));
        
        booking.setPassengerList(passengerFeign.getPassengerByBookingId(id));
        
        logger.info("Booking fetched successfully: " + booking.toString());
        
        return booking;
    }
    
    @Override
	public List<Booking> getBookingsByBusId(int busId) {
        logger.info("Fetching bookings by Bus ID: " + busId);
        
        List<Booking> bookings = bookingRepository.findByBusId(busId);
        if (bookings.isEmpty()) {
            throw new BookingNotFoundException("Booking not found with bus id: " + busId);
        }
        
        return bookings;
    }

    @Override
	public List<Booking> getAllBookings() {
        logger.info("Fetching all bookings.");
        
        List<Booking> allBookings = bookingRepository.findAll();
        if (allBookings.isEmpty()) {
            throw new BookingNotFoundException("No bookings done yet");
        }
        for(Booking booking:allBookings) {
			booking.setPassengerList(passengerFeign.getPassengerByBookingId(booking.getBookingId()));
		}
        
        return allBookings;
    }
    
    @Override
	public Booking deleteBooking(int id) {
        logger.info("Deleting booking with ID: " + id);
        
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + id));
        
        bookingRepository.delete(booking);
        
        return booking;
    }

	@Override
	public String cancelBookingPartiallyByPassengerId(int passengerId) {
		logger.info("Canceling booking partially by Passenger ID: " + passengerId);
		
		Passenger passenger= passengerFeign.deletePassengerById(passengerId).getBody();
		int bkid=passenger.getBookingId();
		List<Booking> list=bookingRepository.findAll();
		if (list.isEmpty()) {
			throw new BookingNotFoundException("No bookings found from cancel booking");
		}
		Booking booking=null;
		for(Booking b:list) {
			if(b.getBookingId()==bkid) {
				booking=b;
			}
		}
		String preference=passenger.getPassengerPrefrence();
		Booking newBooking=refundCalculator(booking, preference);
		newBooking.setNoOfBookings(newBooking.getNoOfBookings()-1);
		bookingRepository.save(newBooking);
		int busId=newBooking.getBusId();
		bookingFeign.updateBusWhileCancellation(busId, preference);
		
		logger.info("Booking canceled partially successfully.");
		
		return "Passenger Deleted Successfully";
	}
	
	@Override
	public Booking refundCalculator(Booking booking,String preference) {
		logger.info("Calculating refund for booking: " + booking.getBookingId());
		
		Map<Long, Double> refundPriceMap=new HashMap<>();
		refundPriceMap.put(0L, 0.0);
		refundPriceMap.put(1L, .5);
		refundPriceMap.put(2L, 0.7);
		
		Bus bus=bookingFeign.findBusById(booking.getBusId()).getBody();
		double currentRef=0;
		
		if(preference.equalsIgnoreCase("Sleeper")) {
			long days= ChronoUnit.DAYS.between(LocalDate.now(), bus.getBusDate());
			if(days<3) {
				currentRef=(refundPriceMap.get(days)*(booking.getSleeperCost()/booking.getTypeSleeperCapacity()));
				booking.setRefund(booking.getRefund()+currentRef);
			}
			else {
				currentRef=((booking.getSleeperCost()/booking.getTypeSleeperCapacity())*.8);
				booking.setRefund(booking.getRefund()+currentRef);
			}
			booking.setSleeperCost(booking.getSleeperCost()-(booking.getSleeperCost()/booking.getTypeSleeperCapacity()));
     		booking.setTypeSleeperCapacity(booking.getTypeSleeperCapacity()-1);
		}
		else {
			long days= ChronoUnit.DAYS.between(LocalDate.now(), bus.getBusDate());
			if(days<3) {
				currentRef=(refundPriceMap.get(days)*(booking.getSititngCost()/booking.getTypeSittingCapacity()));
				booking.setRefund(booking.getRefund()+currentRef);
			}
			else {
				currentRef=((booking.getSititngCost()/booking.getTypeSittingCapacity())*.8);
				booking.setRefund(booking.getRefund()+currentRef);
			}
			booking.setSititngCost(booking.getSititngCost()-(booking.getSititngCost()/booking.getTypeSittingCapacity()));
			booking.setTypeSittingCapacity(booking.getTypeSittingCapacity()-1);
		}
		
		booking.setTotalBookingCost(booking.getTotalBookingCost()-currentRef);
		
		return booking;
	}

	@Override
	public List<Binded> getBookingsByBookingEmail(String bookingEmail) {
		logger.info("Fetching bookings by Booking Email: " + bookingEmail);
		List<Binded> bindedList=new ArrayList<>();
		List<Booking> findByEmail = bookingRepository.findByBookingEmail(bookingEmail);
		if (findByEmail.isEmpty()) {
            throw new BookingNotFoundException("No Booking found with provided the email id");
        }
	for(Booking booking:findByEmail) {
			booking.setPassengerList(passengerFeign.getPassengerByBookingId(booking.getBookingId()));
			Bus bus= bookingFeign.findBusByIdForAdmin(booking.getBusId()).getBody();
			bindedList.add(new Binded(booking,bus));
		}
		
        return bindedList;
	}  
}
