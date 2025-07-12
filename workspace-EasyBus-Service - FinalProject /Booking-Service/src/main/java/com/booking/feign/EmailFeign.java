package com.booking.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.booking.entities.Booking;



@FeignClient(name = "Email", url="localhost:8088")
public interface EmailFeign {
    @PostMapping("/bookingConfirmation")
    public void bookingMail(@RequestBody  Booking booking);
}
