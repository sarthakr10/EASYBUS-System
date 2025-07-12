package com.easybus.customExceptionTest;

import com.easybus.customexception.BusNotFoundException;
import com.easybus.customexception.GlobalException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Dummy controller to simulate exceptions
@RestController
@RequestMapping("/test")
class DummyController {

    @GetMapping("/bus-not-found")
    public String throwBusNotFound() {
        throw new BusNotFoundException("Bus with ID 99 not found");
    }

    @GetMapping("/generic-error")
    public String throwGenericException() {
        throw new RuntimeException("Generic error occurred");
    }
}

@WebMvcTest(DummyController.class)
@Import(GlobalException.class)  // Import your GlobalException handler
class GlobalExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void handleBusNotFoundException_returnsErrorDto() throws Exception {
        mockMvc.perform(get("/test/bus-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Bus with ID 99 not found"))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"));
    }

    @Test
    void handleGenericException_returnsGenericErrorDto() throws Exception {
        mockMvc.perform(get("/test/generic-error"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Something went wrong!!!"))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"));
    }
}
