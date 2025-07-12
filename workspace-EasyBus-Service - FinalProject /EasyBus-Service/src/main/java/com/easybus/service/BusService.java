package com.easybus.service;

import java.time.LocalDate;
import java.util.List;

import com.easybus.entities.Bus;

public interface BusService {

	public Bus addBus(Bus bus);

	public Bus updateBus(Bus bus);

	public Bus updateBusForAdmin(Bus bus);

	public List<Bus> getAllBus();

	public Bus findBusById(int id);

	public Bus findBusByIdForAdmin(int id);

	public List<Bus> findBusBySourceAndDestination(String busSource, String busDestination);

	public List<Bus> findBySourceAndDestinationAndDate(String busSource, String busDestination, LocalDate busDate);

	public List<Bus> findBusByDuration(double busDuration);

	public Bus updateBusById(int id);

	public Bus deleteBusById(int id);

	public Bus updateBusWhileCancellation(int busId, String preference);

	public Bus dynamicBusPrice(Bus bus);

	public List<Bus> dynamicBusPrice(List<Bus> busList);

	public List<String> getAllDestinations();

	public List<String> getAllSources();

}