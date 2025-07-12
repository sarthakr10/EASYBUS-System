import { Component, NgZone, OnInit } from '@angular/core';
import { DataService } from '../../services/data.service';
import { Booking } from '../booking/booking.model';
import { AllServiceService } from '../../services/all-service.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';

declare var Razorpay: any;
@Component({
  selector: 'app-payment',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.css',
})
export class PaymentComponent implements OnInit {
  booking: Booking;
  totalBookingCost: number = 0.0;
  constructor(
    private dataService: DataService,
    private allService: AllServiceService,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService,
    private ngZone: NgZone
  ) {
    if (
      authService.getToken()?.length === 0 ||
      authService.getToken() === null
    ) {
      this.router.navigateByUrl('/login');
    }
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
  }
  ngOnInit(): void {
    this.booking = this.dataService.getBooking();
    this.totalBookingCost = this.dataService.getTotalBookingCost();
  }

  getBookingObjFromDataService() {
    this.booking = this.dataService.getBooking();
    console.log(this.booking);
  }

  getTotalBookingCostFromDataService() {
    this.totalBookingCost = this.dataService.getTotalBookingCost();
    console.log(this.totalBookingCost);
  }

  bookBus() {
    console.log(this.booking);
    this.booking.bookingEmail = this.authService.getUserEmail() || '';
    this.allService
      .createBookingTransaction(this.booking, this.totalBookingCost.toString())
      .subscribe(
        (response) => {
          console.log(response);
          this.openTransactionModal(response);
        },
        (error) => {
          console.log(error);
        }
      );
  }
  openTransactionModal(response: any) {
    var options = {
      order_id: response.order_id,
      key: response.key,
      amount: response.amount,
      currency: response.currency,
      name: "Paying to Vaibhav's Bus Service",
      description: 'Payment of Booking',
      image:
        'https://media.istockphohttps://wallpapercave.com/wp/wp9185491.jpgto.com/id/184347141/photo/white-mid-sized-passenger-jet-airplane.jpg?s=1024x1024&w=is&k=20&c=5sh-SrigUNHf1ynqp60F2Ss1wvvcZygQa-__zh5j-gE=',
      handler: (response: any) => {
        if (response != null && response.razorpay_payment_id != null) {
          this.processResponse(response);
        } else {
          alert('Payment failed');
        }
      },
      theme: {
        color: '#F37254',
      },
      prefill: {
        name: 'Vaibhav',
        email: 'vaibhav.nigam@capgemini.com',
        contact: '9630520663',
      },
      notes: {
        address: 'Bus Booking',
      },
    };

    var razorpay = new Razorpay(options);
    razorpay.open();
  }
  processResponse(resp: any) {
    console.log(resp);
    this.booking.userName = this.authService.getUserName() || '';
    this.allService.addBooking(this.booking).subscribe((val) => {
      this.toastr.success('Booking Successful!', 'Success!');
      // this.router.navigateByUrl('/showAll');
      this.ngZone.run(() => {
        this.router.navigateByUrl('/showAll');
      });
    });
  }
}
