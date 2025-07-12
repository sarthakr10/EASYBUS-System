package com.easybus.Service;

import com.easybus.customexception.BusNotFoundException;
import com.easybus.entities.Booking;
import com.easybus.entities.Bus;
import com.easybus.feign.EasyBusFeign;
import com.easybus.repository.BusRepository;
import com.easybus.service.BusServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BusServiceImplTest {

    @Mock
    private BusRepository busRepository;

    @Mock
    private EasyBusFeign easyBusFeign;

    @InjectMocks
    private BusServiceImpl busService;

    private Bus testBus;
    private List<Bus> testBusList;

    @BeforeEach
    void setUp() {
        testBus = new Bus();
        testBus.setBusId(1);
        testBus.setBusName("TestBus");
        testBus.setBusSource("SourceCity");
        testBus.setBusDestination("DestCity");
        testBus.setBusDate(LocalDate.now().plusDays(2));
        testBus.setDepartureFromSource(LocalTime.of(10, 0));
        testBus.setArrivalOnDestination(LocalTime.of(16, 0));
        testBus.setSittingCapacity(40);
        testBus.setTypeSittingCapacity(20);
        testBus.setTypeSleeperCapacity(20);
        testBus.setPriceSitting(1000.0);
        testBus.setPriceSleeper(2000.0);

        testBusList = new ArrayList<>();
        testBusList.add(testBus);
    }

    @Test
    void testAddBus_Success() {
        when(busRepository.findAll()).thenReturn(new ArrayList<>());
        when(busRepository.save(any(Bus.class))).thenReturn(testBus);

        Bus addedBus = busService.addBus(testBus);

        assertNotNull(addedBus);
        assertEquals(1, addedBus.getBusId());
        assertEquals("TestBus", addedBus.getBusName());
        assertEquals(6.0, addedBus.getBusDuration());
        verify(busRepository, times(1)).save(any(Bus.class));
    }
    
    @Test
    void testAddBus_WithSameBusIdAndName_ThrowsException() {
        List<Bus> existingBuses = new ArrayList<>();
        existingBuses.add(testBus);
        
        when(busRepository.findAll()).thenReturn(existingBuses);
        
        Bus newBus = new Bus();
        newBus.setBusId(1);
        newBus.setBusName("TestBus");
        
        assertThrows(BusNotFoundException.class, () -> busService.addBus(newBus));
        verify(busRepository, never()).save(any(Bus.class));
    }

    @Test
    void testAddBus_CapacityMismatch_ThrowsException() {
        testBus.setTypeSittingCapacity(10);
        testBus.setTypeSleeperCapacity(10);
        testBus.setSittingCapacity(30); // Should be 20 (10+10)

        when(busRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(BusNotFoundException.class, () -> busService.addBus(testBus));
    }

    @Test
    void testAddBus_PastDate_ThrowsException() {
        testBus.setBusDate(LocalDate.now().minusDays(1));

        when(busRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(BusNotFoundException.class, () -> busService.addBus(testBus));
    }

    @Test
    void testUpdateBus_Success() {
        when(busRepository.save(any(Bus.class))).thenReturn(testBus);

        Bus updatedBus = busService.updateBus(testBus);

        assertNotNull(updatedBus);
        assertEquals(6.0, updatedBus.getBusDuration());
        verify(busRepository, times(1)).save(any(Bus.class));
    }
    
    @Test
    void testUpdateBusForAdmin_Success() {
        when(busRepository.save(any(Bus.class))).thenReturn(testBus);

        Bus updatedBus = busService.updateBusForAdmin(testBus);

        assertNotNull(updatedBus);
        assertEquals(6.0, updatedBus.getBusDuration());
        verify(busRepository, times(1)).save(any(Bus.class));
    }

    @Test
    void testGetAllBus_Success() {
        when(busRepository.findAll()).thenReturn(testBusList);

        List<Bus> result = busService.getAllBus();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TestBus", result.get(0).getBusName());
    }

    @Test
    void testGetAllBus_EmptyList_ThrowsException() {
        when(busRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(BusNotFoundException.class, () -> busService.getAllBus());
    }

    @Test
    void testFindBusById_Success() {
        when(busRepository.findById(1)).thenReturn(Optional.of(testBus));

        Bus foundBus = busService.findBusById(1);

        assertNotNull(foundBus);
        assertEquals(1, foundBus.getBusId());
    }

    @Test
    void testFindBusById_NotFound_ThrowsException() {
        when(busRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(BusNotFoundException.class, () -> busService.findBusById(999));
    }

    @Test
    void testFindBusById_PastDate_ThrowsException() {
        testBus.setBusDate(LocalDate.now().minusDays(1));
        when(busRepository.findById(1)).thenReturn(Optional.of(testBus));

        assertThrows(BusNotFoundException.class, () -> busService.findBusById(1));
    }

    @Test
    void testFindBusByIdForAdmin_Success() {
        when(busRepository.findById(1)).thenReturn(Optional.of(testBus));

        Bus foundBus = busService.findBusByIdForAdmin(1);

        assertNotNull(foundBus);
        assertEquals(1, foundBus.getBusId());
    }

    @Test
    void testFindBusBySourceAndDestination_Success() {
        when(busRepository.findByBusSourceAndBusDestination("SourceCity", "DestCity")).thenReturn(testBusList);

        List<Bus> result = busService.findBusBySourceAndDestination("SourceCity", "DestCity");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("SourceCity", result.get(0).getBusSource());
        assertEquals("DestCity", result.get(0).getBusDestination());
    }

    @Test
    void testFindBusBySourceAndDestination_NotFound_ThrowsException() {
        when(busRepository.findByBusSourceAndBusDestination("NonExistent", "NonExistent")).thenReturn(new ArrayList<>());

        assertThrows(BusNotFoundException.class, () -> 
            busService.findBusBySourceAndDestination("NonExistent", "NonExistent"));
    }

    @Test
    void testFindBySourceAndDestinationAndDate_Success() {
        LocalDate testDate = LocalDate.now().plusDays(2);
        when(busRepository.findByBusSourceAndBusDestinationAndBusDate("SourceCity", "DestCity", testDate))
            .thenReturn(testBusList);

        List<Bus> result = busService.findBySourceAndDestinationAndDate("SourceCity", "DestCity", testDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testDate, result.get(0).getBusDate());
    }

    @Test
    void testFindBySourceAndDestinationAndDate_PastDate_ThrowsException() {
        LocalDate pastDate = LocalDate.now().minusDays(1);

        assertThrows(BusNotFoundException.class, () -> 
            busService.findBySourceAndDestinationAndDate("SourceCity", "DestCity", pastDate));
    }

    @Test
    void testFindBySourceAndDestinationAndDate_SameDayBeforeDeparture() {
        LocalDate today = LocalDate.now();
        testBus.setBusDate(today);
        testBus.setDepartureFromSource(LocalTime.now().plusHours(2)); // 2 hours from now
        
        when(busRepository.findByBusSourceAndBusDestinationAndBusDate("SourceCity", "DestCity", today))
            .thenReturn(testBusList);

        List<Bus> result = busService.findBySourceAndDestinationAndDate("SourceCity", "DestCity", today);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindBusByDuration_Success() {
        when(busRepository.findByBusDurationIsLessThanEqual(10.0)).thenReturn(testBusList);

        List<Bus> result = busService.findBusByDuration(10.0);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindBusByDuration_NotFound_ThrowsException() {
        when(busRepository.findByBusDurationIsLessThanEqual(1.0)).thenReturn(new ArrayList<>());

        assertThrows(BusNotFoundException.class, () -> busService.findBusByDuration(1.0));
    }

    @Test
    void testUpdateBusById_Success() {
        when(busRepository.findById(1)).thenReturn(Optional.of(testBus));
        
        List<Booking> mockBookings = new ArrayList<>();
        Booking booking = new Booking();
        booking.setTypeSleeperCapacity(2);
        booking.setTypeSittingCapacity(3);
        mockBookings.add(booking);
        
        when(easyBusFeign.getBookingsByBusId(1)).thenReturn(mockBookings);
        when(busRepository.save(any(Bus.class))).thenReturn(testBus);

        Bus result = busService.updateBusById(1);

        assertNotNull(result);
        assertEquals(18, result.getTypeSleeperCapacity()); // 20 - 2
        assertEquals(17, result.getTypeSittingCapacity()); // 20 - 3
    }

    @Test
    void testUpdateBusById_NoBookings_ThrowsException() {
        when(busRepository.findById(1)).thenReturn(Optional.of(testBus));
        when(easyBusFeign.getBookingsByBusId(1)).thenReturn(new ArrayList<>());

        assertThrows(BusNotFoundException.class, () -> busService.updateBusById(1));
    }

    @Test
    void testDeleteBusById_Success() {
        when(busRepository.findById(1)).thenReturn(Optional.of(testBus));
        doNothing().when(busRepository).delete(any(Bus.class));

        Bus deletedBus = busService.deleteBusById(1);

        assertNotNull(deletedBus);
        assertEquals(1, deletedBus.getBusId());
        verify(busRepository, times(1)).delete(any(Bus.class));
    }

    @Test
    void testDeleteBusById_NotFound_ThrowsException() {
        when(busRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(BusNotFoundException.class, () -> busService.deleteBusById(999));
    }

    @Test
    void testUpdateBusWhileCancellation_SleeperPreference() {
        when(busRepository.findById(1)).thenReturn(Optional.of(testBus));
        when(busRepository.save(any(Bus.class))).thenReturn(testBus);

        Bus result = busService.updateBusWhileCancellation(1, "Sleeper");

        assertEquals(21, result.getTypeSleeperCapacity()); // 20 + 1
        assertEquals(20, result.getTypeSittingCapacity()); // Unchanged
    }

    @Test
    void testUpdateBusWhileCancellation_SittingPreference() {
        when(busRepository.findById(1)).thenReturn(Optional.of(testBus));
        when(busRepository.save(any(Bus.class))).thenReturn(testBus);

        Bus result = busService.updateBusWhileCancellation(1, "Sitting");

        assertEquals(20, result.getTypeSleeperCapacity()); // Unchanged
        assertEquals(21, result.getTypeSittingCapacity()); // 20 + 1
    }

    @Test
    void testDynamicBusPrice_HighDemand() {
        // Less than 1 day before departure
        testBus.setBusDate(LocalDate.now().plusDays(0));
        
        Bus result = busService.dynamicBusPrice(testBus);
        
        assertEquals(1400.0, result.getPriceSitting()); // 1000 * 1.4
        assertEquals(2800.0, result.getPriceSleeper()); // 2000 * 1.4
    }
    
    @Test
    void testDynamicBusPrice_MediumDemand() {
        // Less than 3 days before departure
        testBus.setBusDate(LocalDate.now().plusDays(2));
        
        Bus result = busService.dynamicBusPrice(testBus);
        
        assertEquals(1200.0, result.getPriceSitting()); // 1000 * 1.2
        assertEquals(2400.0, result.getPriceSleeper()); // 2000 * 1.2
    }
    
    @Test
    void testDynamicBusPrice_LowDemand() {
        // Less than 5 days before departure
        testBus.setBusDate(LocalDate.now().plusDays(4));
        
        Bus result = busService.dynamicBusPrice(testBus);
        
        assertEquals(1100.0, result.getPriceSitting()); // 1000 * 1.1
        assertEquals(2200.0, result.getPriceSleeper()); // 2000 * 1.1
    }
    
    @Test
    void testDynamicBusPriceList() {
        List<Bus> busList = new ArrayList<>();
        
        Bus bus1 = new Bus();
        bus1.setBusDate(LocalDate.now().plusDays(0));
        bus1.setPriceSitting(1000.0);
        bus1.setPriceSleeper(2000.0);
        bus1.setTypeSittingCapacity(10);
        bus1.setTypeSleeperCapacity(5);
        
        Bus bus2 = new Bus();
        bus2.setBusDate(LocalDate.now().plusDays(2));
        bus2.setPriceSitting(1000.0);
        bus2.setPriceSleeper(2000.0);
        bus2.setTypeSittingCapacity(10);
        bus2.setTypeSleeperCapacity(5);
        
        busList.add(bus1);
        busList.add(bus2);
        
        List<Bus> result = busService.dynamicBusPrice(busList);
        
        assertEquals(1400.0, result.get(0).getPriceSitting()); // 1000 * 1.4
        assertEquals(2800.0, result.get(0).getPriceSleeper()); // 2000 * 1.4
        
        assertEquals(1200.0, result.get(1).getPriceSitting()); // 1000 * 1.2
        assertEquals(2400.0, result.get(1).getPriceSleeper()); // 2000 * 1.2
    }
    
    @Test
    void testGetAllDestinations_Success() {
        when(busRepository.findAll()).thenReturn(testBusList);
        
        List<String> result = busService.getAllDestinations();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("DestCity", result.get(0));
    }
    
    @Test
    void testGetAllDestinations_NoBuses_ThrowsException() {
        when(busRepository.findAll()).thenReturn(new ArrayList<>());
        
        assertThrows(BusNotFoundException.class, () -> busService.getAllDestinations());
    }
    
    @Test
    void testGetAllSources_Success() {
        when(busRepository.findAll()).thenReturn(testBusList);
        
        List<String> result = busService.getAllSources();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("SourceCity", result.get(0));
    }
    
    @Test
    void testGetAllSources_NoBuses_ThrowsException() {
        when(busRepository.findAll()).thenReturn(new ArrayList<>());
        
        assertThrows(BusNotFoundException.class, () -> busService.getAllSources());
    }
}