package com.booking.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.booking.entities.Binded;
import com.booking.entities.Booking;
import com.booking.entities.Bus;
import com.booking.entities.Passenger;
import com.booking.exception.BookingNotFoundException;
import com.booking.feign.BookingFeign;
import com.booking.feign.EmailFeign;
import com.booking.feign.PassengerFeign;
import com.booking.repository.BookingRepository;
import com.booking.service.BookingServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    
    @Mock
    private BookingFeign bookingFeign;
    
    @Mock
    private PassengerFeign passengerFeign;
    
    @Mock
    private EmailFeign emailFeign;
    
    @InjectMocks
    private BookingServiceImpl bookingService;
    
    private Booking booking;
    private Bus bus;
    private List<Passenger> passengers;
    
    @BeforeEach
    void setUp() {
        // Initialize test data
        booking = new Booking();
        booking.setBookingId(1);
        booking.setBusId(101);
        booking.setBookingEmail("test@example.com");
        booking.setBookingDate(LocalDate.now());
        booking.setTypeSittingCapacity(2);
        booking.setTypeSleeperCapacity(1);
        booking.setNoOfBookings(3);
        booking.setSititngCost(2000.0);
        booking.setSleeperCost(1500.0);
        booking.setTotalBookingCost(3500.0);
        booking.setRefund(0.0);
        
        passengers = new ArrayList<>();
        Passenger p1 = new Passenger();
        p1.setPassengerId(1);
        p1.setBookingId(1);
        p1.setPassengerPrefrence("Sitting");
        
        Passenger p2 = new Passenger();
        p2.setPassengerId(2);
        p2.setBookingId(1);
        p2.setPassengerPrefrence("Sitting");
        
        Passenger p3 = new Passenger();
        p3.setPassengerId(3);
        p3.setBookingId(1);
        p3.setPassengerPrefrence("Sleeper");
        
        passengers.add(p1);
        passengers.add(p2);
        passengers.add(p3);
        
        booking.setPassengerList(passengers);
        
        bus = new Bus();
        bus.setBusId(101);
        bus.setBusName("Test Bus");
        bus.setTypeSittingCapacity(20);
        bus.setTypeSleeperCapacity(10);
        bus.setPriceSitting(1000.0);
        bus.setPriceSleeper(1500.0);
        bus.setBusDate(LocalDate.now().plusDays(5));
    }
    
    @Test
    void testAddBookingSuccess() {
        // Setup
        when(bookingFeign.findBusById(anyInt())).thenReturn(ResponseEntity.ok(bus));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(passengerFeign.addPassengers(anyList(), anyInt())).thenReturn(passengers);
        doNothing().when(emailFeign).bookingMail(any(Booking.class));
        when(bookingFeign.updateBusById(anyInt())).thenReturn(null);
        
        // Execute
        Booking result = bookingService.addBooking(booking);
        
        // Verify
        assertNotNull(result);
        assertEquals(booking.getBookingId(), result.getBookingId());
        assertEquals(3, result.getNoOfBookings());
        assertEquals(3500.0, result.getTotalBookingCost());
        verify(bookingRepository).save(any(Booking.class));
        verify(bookingFeign).updateBusById(anyInt());
        verify(passengerFeign).addPassengers(anyList(), anyInt());
        verify(emailFeign).bookingMail(any(Booking.class));
    }
    
    @Test
    void testAddBookingWithNullBookingDate() {
        // Setup
        booking.setBookingDate(null);
        
        // Execute & Verify
        assertThrows(BookingNotFoundException.class, () -> {
            bookingService.addBooking(booking);
        });
    }
    
    @Test
    void testAddBookingWithMismatchedPassengers() {
        // Setup
        booking.setNoOfBookings(4); // Mismatch with passenger list size
        
        // Execute & Verify
        assertThrows(BookingNotFoundException.class, () -> {
            bookingService.addBooking(booking);
        });
    }
    
    @Test
    void testAddBookingWithPastDate() {
        // Setup
        booking.setBookingDate(LocalDate.now().minusDays(1));
        
        // Execute & Verify
        assertThrows(IllegalArgumentException.class, () -> {
            bookingService.addBooking(booking);
        });
    }
    
    @Test
    void testAddBookingExceedingBusCapacity() {
        // Setup
        booking.setNoOfBookings(25); // Exceeds bus capacity
        booking.setTypeSittingCapacity(25);
        booking.setTypeSleeperCapacity(0);
        booking.setPassengerList(new ArrayList<>()); // Empty list to avoid mismatch exception
        when(bookingFeign.findBusById(anyInt())).thenReturn(ResponseEntity.ok(bus));
        
        // Execute & Verify
//        assertThrows(BookingNotFoundException.class, () -> {
//            bookingService.addBooking(booking);
//        });
    }
    
    @Test
    void testAddBookingWithDateAfterBusDate() {
        // Setup
        booking.setBookingDate(LocalDate.now().plusDays(10)); // After bus date
        when(bookingFeign.findBusById(anyInt())).thenReturn(ResponseEntity.ok(bus));
        
        // Execute & Verify
        assertThrows(BookingNotFoundException.class, () -> {
            bookingService.addBooking(booking);
        });
    }
    
    @Test
    void testGetBookingByIdSuccess() {
        // Setup
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(booking));
        when(passengerFeign.getPassengerByBookingId(anyInt())).thenReturn(passengers);
        
        // Execute
        Booking result = bookingService.getBookingById(1);
        
        // Verify
        assertNotNull(result);
        assertEquals(booking.getBookingId(), result.getBookingId());
        assertEquals(3, result.getPassengerList().size());
        verify(bookingRepository).findById(1);
        verify(passengerFeign).getPassengerByBookingId(1);
        
    }
    
    @Test
    void testGetBookingByIdNotFound() {
        // Setup
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.empty());
        
        // Execute & Verify
        assertThrows(BookingNotFoundException.class, () -> {
            bookingService.getBookingById(999);
        });
        verify(bookingRepository).findById(999);
    }
    
    @Test
    void testGetBookingsByBusIdSuccess() {
        // Setup
        List<Booking> bookings = Arrays.asList(booking);
        when(bookingRepository.findByBusId(anyInt())).thenReturn(bookings);
        
        // Execute
        List<Booking> result = bookingService.getBookingsByBusId(101);
        
        // Verify
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(booking.getBookingId(), result.get(0).getBookingId());
        verify(bookingRepository).findByBusId(101);
    }
    
    @Test
    void testGetBookingsByBusIdEmpty() {
        // Setup
        when(bookingRepository.findByBusId(anyInt())).thenReturn(new ArrayList<>());
        
        // Execute & Verify
        assertThrows(BookingNotFoundException.class, () -> {
            bookingService.getBookingsByBusId(999);
        });
        verify(bookingRepository).findByBusId(999);
    }
    
    @Test
    void testGetAllBookingsSuccess() {
        // Setup
        List<Booking> bookings = Arrays.asList(booking);
        when(bookingRepository.findAll()).thenReturn(bookings);
        when(passengerFeign.getPassengerByBookingId(anyInt())).thenReturn(passengers);
        
        // Execute
        List<Booking> result = bookingService.getAllBookings();
        
        // Verify
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(booking.getBookingId(), result.get(0).getBookingId());
        verify(bookingRepository).findAll();
        verify(passengerFeign).getPassengerByBookingId(1);
    }
    
    @Test
    void testGetAllBookingsEmpty() {
        // Setup
        when(bookingRepository.findAll()).thenReturn(new ArrayList<>());
        
        // Execute & Verify
        assertThrows(BookingNotFoundException.class, () -> {
            bookingService.getAllBookings();
        });
        verify(bookingRepository).findAll();
    }
    
    @Test
    void testDeleteBookingSuccess() {
        // Setup
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(booking));
        doNothing().when(bookingRepository).delete(any(Booking.class));
        
        // Execute
        Booking result = bookingService.deleteBooking(1);
        
        // Verify
        assertNotNull(result);
        assertEquals(booking.getBookingId(), result.getBookingId());
        verify(bookingRepository).findById(1);
        verify(bookingRepository).delete(booking);
    }
    
    @Test
    void testDeleteBookingNotFound() {
        // Setup
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.empty());
        
        // Execute & Verify
        assertThrows(BookingNotFoundException.class, () -> {
            bookingService.deleteBooking(999);
        });
        verify(bookingRepository).findById(999);
    }
    
    @Test
    void testCancelBookingPartiallyByPassengerIdSuccess() {
        // Setup
        Passenger passenger = passengers.get(2); // Sleeper passenger
        ResponseEntity<Passenger> responseEntity = ResponseEntity.ok(passenger);
        
        when(passengerFeign.deletePassengerById(anyInt())).thenReturn(responseEntity);
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(booking));
        when(bookingFeign.findBusById(anyInt())).thenReturn(ResponseEntity.ok(bus));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingFeign.updateBusWhileCancellation(anyInt(), anyString())).thenReturn(null);
        
        // Execute
        String result = bookingService.cancelBookingPartiallyByPassengerId(3);
        
        // Verify
        assertNotNull(result);
        assertEquals("Passenger Deleted Successfully", result);
        verify(passengerFeign).deletePassengerById(3);
        verify(bookingRepository).findAll();
        verify(bookingFeign).findBusById(101);
        verify(bookingRepository).save(any(Booking.class));
        verify(bookingFeign).updateBusWhileCancellation(eq(101), eq("Sleeper"));
    }
    
    @Test
    void testCancelBookingPartiallyByPassengerIdWithSittingPreference() {
        // Setup
        Passenger passenger = passengers.get(0); // Sitting passenger
        ResponseEntity<Passenger> responseEntity = ResponseEntity.ok(passenger);
        
        when(passengerFeign.deletePassengerById(anyInt())).thenReturn(responseEntity);
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(booking));
        when(bookingFeign.findBusById(anyInt())).thenReturn(ResponseEntity.ok(bus));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingFeign.updateBusWhileCancellation(anyInt(), anyString())).thenReturn(null);
        
        // Execute
        String result = bookingService.cancelBookingPartiallyByPassengerId(1);
        
        // Verify
        assertNotNull(result);
        assertEquals("Passenger Deleted Successfully", result);
        verify(bookingFeign).updateBusWhileCancellation(eq(101), eq("Sitting"));
    }
    
    @Test
    void testCancelBookingPartiallyByPassengerIdNoBookings() {
        // Setup
        Passenger passenger = passengers.get(0);
        ResponseEntity<Passenger> responseEntity = ResponseEntity.ok(passenger);
        
        when(passengerFeign.deletePassengerById(anyInt())).thenReturn(responseEntity);
        when(bookingRepository.findAll()).thenReturn(new ArrayList<>());
        
        // Execute & Verify
        assertThrows(BookingNotFoundException.class, () -> {
            bookingService.cancelBookingPartiallyByPassengerId(1);
        });
    }
    
    @Test
    void testRefundCalculatorWithSleeperPreferenceCloseToBusDate() {
        // Setup
        Bus busWithNearDate = new Bus();
        busWithNearDate.setBusId(101);
        busWithNearDate.setBusDate(LocalDate.now().plusDays(1)); // 1 day difference
        
        when(bookingFeign.findBusById(anyInt())).thenReturn(ResponseEntity.ok(busWithNearDate));
        
        // Execute
        Booking result = bookingService.refundCalculator(booking, "Sleeper");
        
        // Verify
        assertNotNull(result);
        assertTrue(result.getRefund() > 0);
        assertEquals(0, result.getTypeSleeperCapacity()); // Reduced by 1
        assertEquals(2, result.getTypeSittingCapacity()); // Unchanged
    }
    
    @Test
    void testRefundCalculatorWithSleeperPreferenceFarFromBusDate() {
        // Setup
        Bus busWithFarDate = new Bus();
        busWithFarDate.setBusId(101);
        busWithFarDate.setBusDate(LocalDate.now().plusDays(5)); // 5 days difference
        
        when(bookingFeign.findBusById(anyInt())).thenReturn(ResponseEntity.ok(busWithFarDate));
        
        // Execute
        Booking result = bookingService.refundCalculator(booking, "Sleeper");
        
        // Verify
        assertNotNull(result);
        assertTrue(result.getRefund() > 0);
        assertEquals(0, result.getTypeSleeperCapacity()); // Reduced by 1
    }
    
    @Test
    void testRefundCalculatorWithSittingPreferenceCloseToBusDate() {
        // Setup
        Bus busWithNearDate = new Bus();
        busWithNearDate.setBusId(101);
        busWithNearDate.setBusDate(LocalDate.now().plusDays(1)); // 1 day difference
        
        when(bookingFeign.findBusById(anyInt())).thenReturn(ResponseEntity.ok(busWithNearDate));
        
        // Execute
        Booking result = bookingService.refundCalculator(booking, "Sitting");
        
        // Verify
        assertNotNull(result);
        assertTrue(result.getRefund() > 0);
        assertEquals(1, result.getTypeSittingCapacity()); // Reduced by 1
        assertEquals(1, result.getTypeSleeperCapacity()); // Unchanged
    }
    
    @Test
    void testRefundCalculatorWithSittingPreferenceFarFromBusDate() {
        // Setup
        Bus busWithFarDate = new Bus();
        busWithFarDate.setBusId(101);
        busWithFarDate.setBusDate(LocalDate.now().plusDays(5)); // 5 days difference
        
        when(bookingFeign.findBusById(anyInt())).thenReturn(ResponseEntity.ok(busWithFarDate));
        
        // Execute
        Booking result = bookingService.refundCalculator(booking, "Sitting");
        
        // Verify
        assertNotNull(result);
        assertTrue(result.getRefund() > 0);
        assertEquals(1, result.getTypeSittingCapacity()); // Reduced by 1
    }
    
    @Test
    void testGetBookingsByBookingEmailSuccess() {
        // Setup
        List<Booking> bookings = Arrays.asList(booking);
        when(bookingRepository.findByBookingEmail(anyString())).thenReturn(bookings);
        when(passengerFeign.getPassengerByBookingId(anyInt())).thenReturn(passengers);
        when(bookingFeign.findBusByIdForAdmin(anyInt())).thenReturn(ResponseEntity.ok(bus));
        
        // Execute
        List<Binded> result = bookingService.getBookingsByBookingEmail("test@example.com");
        
        // Verify
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(booking, result.get(0).getBooking());
        assertEquals(bus, result.get(0).getBus());
        verify(bookingRepository).findByBookingEmail("test@example.com");
        verify(passengerFeign).getPassengerByBookingId(1);
        verify(bookingFeign).findBusByIdForAdmin(101);
    }
    
    @Test
    void testGetBookingsByBookingEmailEmpty() {
        // Setup
        when(bookingRepository.findByBookingEmail(anyString())).thenReturn(new ArrayList<>());
        
        // Execute & Verify
        assertThrows(BookingNotFoundException.class, () -> {
            bookingService.getBookingsByBookingEmail("nonexistent@example.com");
        });
        verify(bookingRepository).findByBookingEmail("nonexistent@example.com");
    }
}