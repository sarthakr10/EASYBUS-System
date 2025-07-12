package com.passenger.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.passenger.controller.PassengerController;
import com.passenger.service.PassengerService;

public class PassengerControllerTest {

    @InjectMocks
    private PassengerController passengerController;

    @Mock
    private PassengerService passengerService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddPassengers() {
        int bookingId = 1;
        List<Passenger> passengersToAdd = Arrays.asList(new Passenger(), new Passenger());

        when(passengerService.addPassengers(anyList(), eq(bookingId))).thenReturn(passengersToAdd);

        List<Passenger> addedPassengers = passengerController.addPassengers(passengersToAdd, bookingId);

        assertEquals(2, addedPassengers.size());
    }

    @Test
    public void testGetPassengerByBookingId() {
        int bookingId = 1;
        List<Passenger> mockPassengers = Arrays.asList(new Passenger(), new Passenger());

        when(passengerService.getPassengerByBookingId(bookingId)).thenReturn(ResponseEntity.ok(mockPassengers));

        ResponseEntity<List<Passenger>> response = passengerController.getPassengerByBookingId(bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testDeletePassengerById() {
        int passengerId = 1;
        Passenger mockPassenger = new Passenger();
        mockPassenger.setPassengerId(passengerId);

        when(passengerService.deletePassengerById(passengerId)).thenReturn(ResponseEntity.ok(mockPassenger));

        ResponseEntity<Passenger> response = passengerController.deletePassengerById(passengerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(passengerId, response.getBody().getPassengerId());
    }
}
