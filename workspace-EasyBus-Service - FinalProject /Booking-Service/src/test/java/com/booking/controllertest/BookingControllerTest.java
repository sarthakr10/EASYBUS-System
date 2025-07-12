package com.booking.controllertest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.booking.controller.BookingController;
import com.booking.entities.Binded;
import com.booking.entities.Booking;
import com.booking.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private Booking testBooking;
    private List<Booking> bookingList;
    private List<Binded> bindedList;

    @BeforeEach
    void setUp() {
        // Set up test data
        testBooking = new Booking();
        testBooking.setBookingId(1);
        testBooking.setBusId(101);
        testBooking.setBookingEmail("test@example.com");
       

        bookingList = Arrays.asList(testBooking);

        Binded bindedBooking = new Binded();
       
        bindedList = Arrays.asList(bindedBooking);
    }

    @Test
    void testAddBooking() throws Exception {
        when(bookingService.addBooking(any(Booking.class))).thenReturn(testBooking);

        mockMvc.perform(post("/booking/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBooking)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookingId").value(1))
                .andExpect(jsonPath("$.busId").value(101))
                .andExpect(jsonPath("$.bookingEmail").value("test@example.com"));
    }

    @Test
    void testGetBookingById() throws Exception {
        when(bookingService.getBookingById(anyInt())).thenReturn(testBooking);

        mockMvc.perform(get("/booking/get/{bookingId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1))
                .andExpect(jsonPath("$.busId").value(101))
                .andExpect(jsonPath("$.bookingEmail").value("test@example.com"));
    }

    @Test
    void testGetBookingsByBusId() throws Exception {
        when(bookingService.getBookingsByBusId(anyInt())).thenReturn(bookingList);

        mockMvc.perform(get("/booking/getByBusId/{busId}", 101))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingId").value(1))
                .andExpect(jsonPath("$[0].busId").value(101))
                .andExpect(jsonPath("$[0].bookingEmail").value("test@example.com"));
    }

    @Test
    void testGetAllBookings() throws Exception {
        when(bookingService.getAllBookings()).thenReturn(bookingList);

        mockMvc.perform(get("/booking/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingId").value(1))
                .andExpect(jsonPath("$[0].busId").value(101))
                .andExpect(jsonPath("$[0].bookingEmail").value("test@example.com"));
    }

    @Test
    void testDeleteBooking() throws Exception {
        when(bookingService.deleteBooking(anyInt())).thenReturn(testBooking);

        mockMvc.perform(delete("/booking/delete/{bookingId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1))
                .andExpect(jsonPath("$.busId").value(101))
                .andExpect(jsonPath("$.bookingEmail").value("test@example.com"));
    }

    @Test
    void testCancelBookingPartiallyByPassengerId() throws Exception {
        String successMsg = "Booking successfully canceled";
        when(bookingService.cancelBookingPartiallyByPassengerId(anyInt())).thenReturn(successMsg);

        mockMvc.perform(delete("/booking/cancelBooking/{passengerId}", 201))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(successMsg));
    }

    @Test
    void testGetBookingsByBookingEmail() throws Exception {
        when(bookingService.getBookingsByBookingEmail(anyString())).thenReturn(bindedList);

        mockMvc.perform(get("/booking/getByBookingEmail/{bookingEmail}", "test@example.com"))
                .andExpect(status().isOk());
    }
}