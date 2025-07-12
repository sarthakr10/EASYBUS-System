import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DataService } from '../../services/data.service';
import Bus from '../bus/bus.model';
import { Booking } from './booking.model';
import { AllServiceService } from '../../services/all-service.service';
import { Passenger } from '../../models/Passenger.model';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-booking',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './booking.component.html',
  styleUrl: './booking.component.css',
})
export class BookingComponent implements OnInit {
  busDetails: Bus;
  booking: Booking;
  newPassenger: Passenger;
  totalBookingCost: number = 0.0;

  numBookings: number = 0;
  numOfSittingSeats: number = 0;
  numOfSleeperSeats: number = 0;
  passengers: Passenger[] = [];
  isPassengerFormVisible = false;

  constructor(private dataService: DataService, private router: Router) {
    this.busDetails = {
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
    };

    this.booking = {
      bookingId: 0,
      busId: 0,
      noOfBookings: 0,
      typeSittingCapacity: 0,
      typeSleeperCapacity: 0,
      passengerList: [],
      bookingEmail: '',
      refund: 0,
      userName: '',
    };

    this.newPassenger = {
      passengerId: 0,
      passengerName: '',
      passengerAge: 0,
      passengerGender: '',
      passengerPhoneNumber: 0,
      passengerPrefrence: '',
    };
  }

  ngOnInit(): void {
    this.busDetails = this.dataService.getBus();
  }

  showPassengerForm(): void {
    this.isPassengerFormVisible = true;
  }

  onSubmit(passengerForm: any): void {
    if (passengerForm.valid) {
      this.addPassenger();
    } else {
      passengerForm.form.markAllAsTouched();
    }
  }

  addPassenger(): void {
    if (
      this.newPassenger.passengerName &&
      this.newPassenger.passengerAge &&
      this.newPassenger.passengerGender &&
      this.newPassenger.passengerPrefrence
    ) {
      // Check seat availability before adding the passenger
      if (
        (this.newPassenger.passengerPrefrence === 'Sitting' &&
          this.numOfSittingSeats < this.busDetails.typeSittingCapacity) ||
        (this.newPassenger.passengerPrefrence === 'Sleeper' &&
          this.numOfSleeperSeats < this.busDetails.typeSleeperCapacity)
      ) {
        // Add passenger to the list
        this.passengers.push({ ...this.newPassenger });

        // Update seat counts
        if (this.newPassenger.passengerPrefrence === 'Sitting') {
          this.numOfSittingSeats += 1;
        } else {
          this.numOfSleeperSeats += 1;
        }

        this.newPassenger = {
          passengerId: 0,
          passengerName: '',
          passengerAge: 0,
          passengerGender: '',
          passengerPhoneNumber: 0,
          passengerPrefrence: '',
        };
      } else {
        alert('No more seats available in the selected class.');
      }
    } else {
      console.log('Please fill in all passenger details.');
    }
  }

  bookBus(): void {
    this.booking.busId = this.busDetails.busId;
    this.booking.noOfBookings = this.numOfSittingSeats + this.numOfSleeperSeats;
    this.booking.typeSittingCapacity = this.numOfSittingSeats;
    this.booking.typeSleeperCapacity = this.numOfSleeperSeats;
    this.booking.passengerList = this.passengers;
    this.totalBookingCost =
      this.booking.typeSittingCapacity * this.busDetails.priceSitting +
      this.booking.typeSleeperCapacity * this.busDetails.priceSleeper;

    this.dataService.setBooking(this.booking, this.totalBookingCost);
    console.log(this.booking);
    this.router.navigateByUrl('/payment');
  }

  isAddPassengerDisabled(): boolean {
    return (
      (this.newPassenger.passengerPrefrence === 'Sitting' &&
        this.numOfSittingSeats >= this.busDetails.typeSittingCapacity) ||
      (this.newPassenger.passengerPrefrence === 'Sleeper' &&
        this.numOfSleeperSeats >= this.busDetails.typeSleeperCapacity)
    );
  }
}
