package com.booking.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.booking.entities.Bus;

@FeignClient(name = "EASYBUS-SERVICE/bus")
public interface BookingFeign {
	
	@PutMapping("/update/{id}")
	public Bus updateBusById(@PathVariable("id") int id);
	
	@PostMapping("updateCancel/{busId}/{preference}")
    ResponseEntity<Bus> updateBusWhileCancellation(@PathVariable("busId") int busId,@PathVariable("preference") String preference);
	
	@GetMapping("/getById/{id}")
    public ResponseEntity<Bus> findBusById(@PathVariable("id") int id);
	
	@GetMapping("/getByIdForAdmin/{id}")
    public ResponseEntity<Bus> findBusByIdForAdmin(@PathVariable("id") int id);
}
