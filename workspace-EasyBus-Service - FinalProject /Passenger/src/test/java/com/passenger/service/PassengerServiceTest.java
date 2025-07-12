package com.passenger.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.passenger.entities.Passenger;
import com.passenger.repository.PassengerRepository;

@SpringBootTest
public class PassengerServiceTest {

    @Autowired
    private PassengerService passengerService;

    @MockBean
    private PassengerRepository passengerRepository;

    private List<Passenger> testPassengers;
    private int bookingId = 101;

    @BeforeEach
    void setUp() {
        testPassengers = Arrays.asList(new Passenger(), new Passenger());
        for (Passenger p : testPassengers) {
            p.setBookingId(bookingId);
        }
    }
    
    @Test
    void testAddPassengers() {
        when(passengerRepository.save(any(Passenger.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Passenger> result = passengerService.addPassengers(testPassengers, bookingId);

        assertThat(result).hasSize(testPassengers.size());
        for (Passenger p : result) {
            assertThat(p.getBookingId()).isEqualTo(bookingId);
        }
    }

    @Test
    void testGetPassengerByBookingId() {
        when(passengerRepository.findByBookingId(anyInt())).thenReturn(testPassengers);

        ResponseEntity<List<Passenger>> response = passengerService.getPassengerByBookingId(bookingId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(testPassengers.size());
    }
    
    @Test
    void testDeletePassengerById() throws Exception {
        Passenger passengerMock = new Passenger();
        when(passengerRepository.findById(anyInt())).thenReturn(Optional.of(passengerMock));

        ResponseEntity<Passenger> response = passengerService.deletePassengerById(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(passengerRepository, times(1)).deleteById(anyInt());
    }


}
