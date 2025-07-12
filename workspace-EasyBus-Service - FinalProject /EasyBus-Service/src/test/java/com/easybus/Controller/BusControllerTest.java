package com.easybus.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.easybus.controller.BusController;
import com.easybus.entities.Bus;
import com.easybus.service.BusServiceImpl;

public class BusControllerTest {

    @InjectMocks
    private BusController busController;

    @Mock
    private BusServiceImpl busService;

    @BeforeEach
    public void init() {
        // Use openMocks instead of deprecated initMocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddBus() {
        // Create a mock Bus object for testing
        Bus busToAdd = new Bus();
        Bus savedBus = new Bus();
        
        when(busService.addBus(any(Bus.class))).thenReturn(savedBus);

        ResponseEntity<Bus> response = busController.addBus(busToAdd);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedBus, response.getBody());
        verify(busService).addBus(busToAdd);
    }

    @Test
    public void testUpdateBus() {
        Bus busToUpdate = new Bus();
        Bus updatedBus = new Bus();
        
        when(busService.updateBus(any(Bus.class))).thenReturn(updatedBus);

        ResponseEntity<Bus> response = busController.updateBus(busToUpdate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBus, response.getBody());
        verify(busService).updateBus(busToUpdate);
    }
    
    @Test
    public void testUpdateBusForAdmin() {
        Bus busToUpdate = new Bus();
        Bus updatedBus = new Bus();
        
        when(busService.updateBusForAdmin(any(Bus.class))).thenReturn(updatedBus);

        ResponseEntity<Bus> response = busController.updateBusForAdmin(busToUpdate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBus, response.getBody());
        verify(busService).updateBusForAdmin(busToUpdate);
    }

    @Test
    public void testGetAllBus_WithResults() {
        List<Bus> mockBusList = Arrays.asList(new Bus(), new Bus());
        when(busService.getAllBus()).thenReturn(mockBusList);

        ResponseEntity<List<Bus>> response = busController.getAllBus();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBusList.size(), response.getBody().size());
        verify(busService).getAllBus();
    }
    
    @Test
    public void testGetAllBus_Empty() {
        List<Bus> emptyList = Collections.emptyList();
        when(busService.getAllBus()).thenReturn(emptyList);

        ResponseEntity<List<Bus>> response = busController.getAllBus();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(busService).getAllBus();
    }

    @Test
    public void testFindBusById() {
        int busId = 1;
        Bus mockBus = new Bus();
        when(busService.findBusById(busId)).thenReturn(mockBus);

        ResponseEntity<Bus> response = busController.findBusById(busId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBus, response.getBody());
        verify(busService).findBusById(busId);
    }

    @Test
    public void testFindBusByIdForAdmin() {
        int busId = 1;
        Bus mockBus = new Bus();
        when(busService.findBusByIdForAdmin(busId)).thenReturn(mockBus);

        ResponseEntity<Bus> response = busController.findBusByIdForAdmin(busId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBus, response.getBody());
        verify(busService).findBusByIdForAdmin(busId);
    }

    @Test
    public void testFindBusBySourceAndDestination() {
        String source = "Mumbai";
        String destination = "Delhi";
        List<Bus> mockBusList = Arrays.asList(new Bus(), new Bus());
        
        when(busService.findBusBySourceAndDestination(source, destination))
                .thenReturn(mockBusList);

        ResponseEntity<List<Bus>> response = busController.findBusBySourceAndDestination(source, destination);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBusList.size(), response.getBody().size());
        verify(busService).findBusBySourceAndDestination(source, destination);
    }

    @Test
    public void testFindBySourceAndDestinationAndDate() {
        String source = "Mumbai";
        String destination = "Delhi";
        String date = "2024-07-03";
        LocalDate parsedDate = LocalDate.parse(date);

        List<Bus> mockBusList = Arrays.asList(new Bus(), new Bus());
        when(busService.findBySourceAndDestinationAndDate(source, destination, parsedDate))
                .thenReturn(mockBusList);

        ResponseEntity<List<Bus>> response = busController.findBySourceAndDestinationAndDate(source, destination, date);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBusList.size(), response.getBody().size());
        verify(busService).findBySourceAndDestinationAndDate(source, destination, parsedDate);
    }

    @Test
    public void testFindBusByDuration() {
        double duration = 8.0;
        List<Bus> mockBusList = Arrays.asList(new Bus(), new Bus());
        when(busService.findBusByDuration(duration)).thenReturn(mockBusList);

        ResponseEntity<List<Bus>> response = busController.findBusByDuration(duration);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBusList.size(), response.getBody().size());
        verify(busService).findBusByDuration(duration);
    }

    @Test
    public void testUpdateBusById() {
        int busId = 1;
        Bus mockBus = new Bus();
        when(busService.updateBusById(busId)).thenReturn(mockBus);

        ResponseEntity<Bus> response = busController.updateBusById(busId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBus, response.getBody());
        verify(busService).updateBusById(busId);
    }
    
    @Test
    public void testDeleteBusById() {
        int busId = 1;
        Bus mockBus = new Bus();
        when(busService.deleteBusById(busId)).thenReturn(mockBus);

        ResponseEntity<Bus> response = busController.deleteBusById(busId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBus, response.getBody());
        verify(busService).deleteBusById(busId);
    }

    @Test
    public void testUpdateBusWhileCancellation() {
        int busId = 1;
        String preference = "refund";
        Bus mockBus = new Bus();
        when(busService.updateBusWhileCancellation(busId, preference))
                .thenReturn(mockBus);

        ResponseEntity<Bus> response = busController.updateBusWhileCancellation(busId, preference);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBus, response.getBody());
        verify(busService).updateBusWhileCancellation(busId, preference);
    }

    @Test
    public void testAllSources() {
        List<String> mockSources = Arrays.asList("Mumbai", "Delhi", "Bangalore");
        when(busService.getAllSources()).thenReturn(mockSources);

        ResponseEntity<List<String>> response = busController.allSources();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockSources, response.getBody());
        verify(busService).getAllSources();
    }

    @Test
    public void testAllDestinations() {
        List<String> mockDestinations = Arrays.asList("Mumbai", "Delhi", "Bangalore");
        when(busService.getAllDestinations()).thenReturn(mockDestinations);

        ResponseEntity<List<String>> response = busController.allDestinations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockDestinations, response.getBody());
        verify(busService).getAllDestinations();
    }
}