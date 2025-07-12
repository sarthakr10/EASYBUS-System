package com.easybus.controller;

import java.time.LocalDate;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.easybus.entities.Bus;
import com.easybus.service.BusService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/bus")
public class BusController {

   private static final Logger logger = LoggerFactory.getLogger(BusController.class);

    @Autowired
    BusService busService;
    
    @PostMapping("/add")
    public ResponseEntity<Bus> addBus(@RequestBody @Valid Bus bus){
    	logger.info("Received request to add bus: {}", bus);
    	Bus savedBus=busService.addBus(bus);
    	return ResponseEntity.status(HttpStatus.CREATED).body(savedBus);
    }

    
    @PostMapping("/update")
    public ResponseEntity<Bus> updateBus(@RequestBody @Valid Bus bus) {
    	Bus updatedBus=busService.updateBus(bus);
        logger.info("Received request to add bus: {}", bus);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBus);
    }
    
    @PostMapping("/updateForAdmin")
    public ResponseEntity<Bus> updateBusForAdmin(@RequestBody @Valid Bus bus) {
    	Bus updatedBus=busService.updateBusForAdmin(bus);
        logger.info("Received request to add bus: {}", bus);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBus);
    }

    @GetMapping("/get")
    public ResponseEntity<List<Bus>> getAllBus() {
        logger.info("Received request to fetch all buses");
        List<Bus> buses = (List<Bus>) busService.getAllBus();
        if (buses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buses);
        }
        return ResponseEntity.ok(buses);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Bus> findBusById(@PathVariable("id") int id) {
        logger.info("Received request to fetch bus by ID: {}", id);
        Bus bus = busService.findBusById(id);
        return ResponseEntity.ok(bus);
    }

    @GetMapping("/getByIdForAdmin/{id}")
    public ResponseEntity<Bus> findBusByIdForAdmin(@PathVariable("id") int id) {
        logger.info("Received request to fetch bus by ID for admin: {}", id);
        Bus bus = busService.findBusByIdForAdmin(id);
        return ResponseEntity.ok(bus);
    }

    @GetMapping("/get/{busSource}/{busDestination}")
    public ResponseEntity<List<Bus>> findBusBySourceAndDestination(@PathVariable("busSource") String busSource,
                                                                   @PathVariable("busDestination") String busDestination) {
        logger.info("Received request to fetch buses from {} to {}", busSource, busDestination);
        List<Bus> buses = busService.findBusBySourceAndDestination(busSource, busDestination);
        return ResponseEntity.ok(buses);
    }

    @GetMapping("/get/{busSource}/{busDestination}/{busDate}")
    public ResponseEntity<List<Bus>> findBySourceAndDestinationAndDate(@PathVariable("busSource") String busSource, 
                                                                       @PathVariable("busDestination") String busDestination,
                                                                       @PathVariable("busDate") String busDate){
        LocalDate newDate = LocalDate.parse(busDate);
        System.err.println(busDate.toString());
        logger.info("Received request to fetch buses from {} to {} on date {}", busSource, busDestination, newDate);
        List<Bus> buses = busService.findBySourceAndDestinationAndDate(busSource,busDestination, newDate);
        return ResponseEntity.ok(buses);
    }
    
    @GetMapping("/getbyduration/{busDuration}")
    public ResponseEntity<List<Bus>> findBusByDuration(@PathVariable("busDuration") double busDuration) {
        logger.info("Received request to fetch buses with duration less than or equal to {}", busDuration);
        List<Bus> buses = busService.findBusByDuration(busDuration);
        return ResponseEntity.ok(buses);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Bus> updateBusById(@PathVariable("id") int id) {
        logger.info("Received request to update bus with ID: {}", id);
        Bus updatedBus = busService.updateBusById(id);
        return ResponseEntity.ok(updatedBus);
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Bus> deleteBusById(@PathVariable("id") int id) {
        Bus deletedBus = busService.deleteBusById(id);
        return ResponseEntity.ok(deletedBus);
    }
    
    @PostMapping("updateCancel/{busId}/{preference}")
    public ResponseEntity<Bus> updateBusWhileCancellation(@PathVariable("busId") int busId, @PathVariable("preference") String preference){
        logger.info("Received request to update bus cancellation details for bus ID: {} with preference: {}", busId, preference);
        Bus updatedBus=busService.updateBusWhileCancellation(busId, preference);
        return ResponseEntity.ok(updatedBus);
    }
    
    
    @GetMapping("/get/sources")
    public ResponseEntity<List<String>> allSources(){
    	return new ResponseEntity<>(busService.getAllSources(),HttpStatus.OK);
    }
    
    @GetMapping("/get/destinations")
    public ResponseEntity<List<String>> allDestinations(){
    	return new ResponseEntity<>(busService.getAllDestinations(),HttpStatus.OK);
    }
}
