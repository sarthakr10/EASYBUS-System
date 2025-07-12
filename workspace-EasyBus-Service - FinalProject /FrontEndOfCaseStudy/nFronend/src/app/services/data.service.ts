import { Injectable } from '@angular/core';
import Bus from '../components/bus/bus.model';
import { Booking } from '../components/booking/booking.model';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  busList: Bus[] = [];
  bus: Bus;
  booking: Booking;
  totalBookingCost: number = 0.0;
  constructor() {
    this.bus = {
      busId: 0,
      busName: '',
      busType: '',
      sittingCapacity: 0,
      typeSleeperCapacity: 0,
      typeSittingCapacity: 0,
      busSource: '',
      busDestination: '',
      busDuration: 0,
      departureFromSource: '',
      arrivalOnDestination: '',
      priceSitting: 0,
      priceSleeper: 0,
      busDate: '',
      // images: [{ url: '' }],
    };
    this.booking = {
      bookingId: 0,
      busId: 0,
      noOfBookings: 0,
      typeSleeperCapacity: 0,
      typeSittingCapacity: 0,
      passengerList: [],
      bookingEmail: '',
      refund: 0,
      userName: '',
    };
  }

  private source: string = '';
  private destination: string = '';
  private date: string = '';
  setSourceDestinationDate(source: string, destination: string, date: string) {
    this.source = source;
    this.destination = destination;
    this.date = date;
  }

  getSourceDestinationDate(): {
    source: string;
    destination: string;
    date: string;
  } {
    return {
      source: this.source,
      destination: this.destination,
      date: this.date,
    };
  }
  setBus(bus: Bus) {
    this.bus = bus;
  }
  getBus(): Bus {
    return this.bus;
  }

  setBooking(
    bookingComingFromBookingComponent: Booking,
    totalBookingCostComingFromBookingComponent: number
  ) {
    this.booking = bookingComingFromBookingComponent;
    console.log(this.booking);
    this.totalBookingCost = totalBookingCostComingFromBookingComponent;
    console.log(this.totalBookingCost);
  }

  getBooking(): Booking {
    return this.booking;
  }
  getTotalBookingCost(): number {
    return this.totalBookingCost;
  }
}
