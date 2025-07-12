import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { AllServiceService } from '../services/all-service.service';
import { Booking } from '../components/booking/booking.model';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import Binded from '../models/binded.model';

@Component({
  selector: 'app-show-all-bookings',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './show-all-bookings.component.html',
  styleUrl: './show-all-bookings.component.css',
})
export class ShowAllBookingsComponent implements OnInit {
  constructor(
    private authService: AuthService,
    private allService: AllServiceService,
    private router: Router
  ) {}
  bindedList: Binded[] = [];

  isDeleted: string = '';
  ngOnInit(): void {
    if (
      this.authService.getToken()?.length === 0 ||
      this.authService.getToken() === null
    ) {
      this.router.navigateByUrl('/login');
    }
    this.allService
      .showAllBookingsByEmail(this.authService.getUserEmail() || '')
      .subscribe((data: Binded[]) => {
        this.bindedList = data;
        console.log(this.bindedList);
        if (this.authService.getToken()?.length === 0) {
          this.router.navigateByUrl('/login');
        }
      });
  }
  deletePassenger(busId: number, passengerId: number) {
    console.log(passengerId);
    this.allService.deletePassenger(passengerId).subscribe((data) => {});
    this.router.navigateByUrl('/showAll');
  }
}
