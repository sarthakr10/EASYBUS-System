package com.controllerTest;


import com.controller.ContactController;
import com.entity.ContactUs;
import com.response.Booking;
import com.services.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;

class ContactControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ContactController contactController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(contactController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testContactEndpoint_shouldSendEmail() throws Exception {
        ContactUs contact = new ContactUs();
        contact.setName("John Doe");
        contact.setEmail("john@example.com");
        contact.setMessage("This is a test message.");
       contact.setSubject("Test Subject");

        mockMvc.perform(post("/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contact)))
                .andExpect(status().isOk());

        verify(emailService, times(1)).sendEmail(
                eq("deekshit2602@gmail.com"),
                eq("Test Subject"),
                contains("Name: John Doe")
        );
    }

    @Test
    void testBookingMailEndpoint_shouldSendBookingConfirmation() throws Exception {
        Booking booking = new Booking();
        booking.setBookingEmail("user@example.com");
        booking.setBookingId((int) 101L);
        booking.setNoOfBookings(2);
        booking.setBusId(555);
        booking.setTypeSittingCapacity(3);
        booking.setTypeSleeperCapacity(1);
        booking.setTotalBookingCost(1500.00);

        mockMvc.perform(post("/bookingConfirmation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isOk());

        verify(emailService, times(1)).sendEmail(
                eq("user@example.com"),
                eq("Booking Confirmation"),
                contains("Booking ID: 101")
        );
    }
}