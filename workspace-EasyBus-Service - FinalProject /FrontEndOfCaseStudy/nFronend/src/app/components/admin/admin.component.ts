import { Component, OnInit } from '@angular/core';
import Bus from '../bus/bus.model';
import { CommonModule } from '@angular/common';
import {
  FormControl,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { AllServiceService } from '../../services/all-service.service';
import { ToastrService } from 'ngx-toastr';
import { Booking } from '../booking/booking.model';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css',
})
export class AdminComponent implements OnInit {
  bus: Bus;
  isAddBusFormVisible = false;
  isBusDetailsVisible = false;
  isUpdateFormVisible = false;
  isShowAllBookingsVisible = false;
  token: string | null = '';
  listOfBuses: Array<Bus> = [];
  listOfBookings: Array<Booking> = [];
  filteredBuses: any[] = [];
  userName: string = '';
  ngOnInit(): void {
    this.token = this.authService.getToken();
    this.userName = this.authService.getUserName()!;
    if (this.token == null) {
      this.router.navigateByUrl('/');
    }
  }

  constructor(
    private authService: AuthService,
    private router: Router,
    private allService: AllServiceService,
    private toastr: ToastrService
  ) {
    if (
      authService.getToken()?.length === 0 ||
      authService.getToken() === null
    ) {
      this.router.navigateByUrl('/login');
    }
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
  }
  editBusDetails(busId: number) {
    this.isBusDetailsVisible = false;
    this.isUpdateFormVisible = true;
    this.allService.getBusByIdForAdmin(busId).subscribe((res: Bus) => {
      this.bus = res; // Update bus object with fetched data
    });
  }
  showAllBuses() {
    this.isBusDetailsVisible = true;
    this.isAddBusFormVisible = false;
    this.isUpdateFormVisible = false;
    this.isShowAllBookingsVisible = false;
    this.allService.getBuses().subscribe({
      next: (res) => {
        this.listOfBuses = res;
        this.filteredBuses = res;
      },
      error: (err) => {
        console.log(err);
        this.toastr.error(err.error.message, 'Error');
      },
      complete: () => {
        this.toastr.success('Bus Loaded successfully', 'Success');
      },
    });
  }
  filterBuses(showFuture: boolean) {
    const today = new Date();
    if (showFuture) {
      this.filteredBuses = this.listOfBuses.filter(
        (bus) => new Date(bus.busDate) > today
      );
    } else {
      this.filteredBuses = this.listOfBuses;
    }
  }
  logout() {
    this.authService.clearToken();
  }
  onSubmit() {
    this.isUpdateFormVisible = false;
    this.allService.addBus(this.bus).subscribe({
      next: (res) => {
        // res.
      },
      error: (err) => {
        console.log(err);
        this.toastr.error(err.error.message, 'Error');
      },
      complete: () => {
        this.toastr.success('Bus added successfully', 'Success');
      },
    });

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
    this.isAddBusFormVisible = false;
    this.isUpdateFormVisible = false;
    this.isShowAllBookingsVisible = false;
  }

  onUpdate() {
    this.isUpdateFormVisible = false;
    this.allService.updateBusForAdmin(this.bus).subscribe({
      next: (res) => {
        // res.
      },
      error: (err) => {
        console.log(err);
        this.toastr.error(err.error.message, 'Error');
      },
      complete: () => {
        this.toastr.success('Bus Updated successfully', 'Success');
      },
    });

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
    this.isAddBusFormVisible = false;
    this.isUpdateFormVisible = false;
    this.isShowAllBookingsVisible = false;
  }
  showAllBookings() {
    this.isShowAllBookingsVisible = true;
    this.isUpdateFormVisible = false;
    this.isBusDetailsVisible = false;
    this.isAddBusFormVisible = false;
    this.allService.showAllBookingsForAdmin().subscribe({
      next: async (res) => {
        this.listOfBookings = res;
        console.log(this.listOfBookings);
      },
      error: (err) => {
        console.log(err);
        this.toastr.error(err.error.message, 'Error');
      },
      complete: () => {
        this.toastr.success('All Bookings Loaded successfully', 'Success');
      },
    });
  }
  updateBus() {
    this.isUpdateFormVisible = true;
    this.isBusDetailsVisible = false;
    this.isAddBusFormVisible = false;
    this.isShowAllBookingsVisible = false;
  }
  addBus() {
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
    this.isAddBusFormVisible = true;
    this.isBusDetailsVisible = false;
    this.isUpdateFormVisible = false;
    this.isShowAllBookingsVisible = false;
  }
}
