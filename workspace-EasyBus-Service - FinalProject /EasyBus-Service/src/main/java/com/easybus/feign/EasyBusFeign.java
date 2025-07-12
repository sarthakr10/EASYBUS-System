package com.easybus.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.easybus.entities.Booking;


@FeignClient(name = "BOOKING-SERVICE/booking")
public interface EasyBusFeign {
	@GetMapping("/getByBusId/{busId}")
	public List<Booking> getBookingsByBusId(@PathVariable("busId") int busId);

}
