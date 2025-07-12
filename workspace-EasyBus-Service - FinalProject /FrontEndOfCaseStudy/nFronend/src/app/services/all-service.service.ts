import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import Bus from '../components/bus/bus.model';
import { Booking } from '../components/booking/booking.model';
import { AuthService } from './auth.service';
import { TransactionDetails } from '../models/TransactionDetails.model';
import ContactUs from '../components/contact-us/Contact.model';
import Binded from '../models/binded.model';

@Injectable({
  providedIn: 'root',
})
export class AllServiceService {
  private baseUrlForBus = 'http://localhost:8086/bus'; // Adjust the URL as per your Spring Boot backend
  private baseUrlForBooking = 'http://localhost:8086/booking'; // Adjust the URL as per your Spring Boot backend
  private apiUrl = 'http://localhost:8088/contact';
  constructor(private http: HttpClient, private authService: AuthService) {}

  addBus(bus: Bus): Observable<Bus> {
    const tkn = this.authService.getToken();

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    return this.http.post<Bus>(`${this.baseUrlForBus}/add`, bus, httpOptions);
  }

  updateBusForAdmin(bus: Bus): Observable<Bus> {
    const tkn = this.authService.getToken();

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    return this.http.post<Bus>(
      `${this.baseUrlForBus}/updateForAdmin`,
      bus,
      httpOptions
    );
  }

  getSources(): Observable<string[]> {
    const tkn = this.authService.getToken();
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    return this.http.get<string[]>(
      `${this.baseUrlForBus}/get/sources`,
      httpOptions
    );
  }

  getDestinations(): Observable<string[]> {
    const tkn = this.authService.getToken();
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    return this.http.get<string[]>(
      `${this.baseUrlForBus}/get/destinations`,
      httpOptions
    );
  }
  getBuses(): Observable<Bus[]> {
    const tkn = this.authService.getToken();

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    return this.http.get<Bus[]>(`${this.baseUrlForBus}/get`, httpOptions);
  }

  getBusById(id: number): Observable<Bus> {
    const tkn = this.authService.getToken();

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    return this.http.get<Bus>(
      `${this.baseUrlForBus}/getById/${id}`,
      httpOptions
    );
  }

  getBusByIdForAdmin(id: number): Observable<Bus> {
    const tkn = this.authService.getToken();

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    return this.http.get<Bus>(
      `${this.baseUrlForBus}/getByIdForAdmin/${id}`,
      httpOptions
    );
  }

  getBusByRouteAndDate(
    source: string,
    destination: string,
    date: string
  ): Observable<Bus[]> {
    const tkn = this.authService.getToken();

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    const url = `${this.baseUrlForBus}/get/${source}/${destination}/${date}`;
    return this.http.get<Bus[]>(url, httpOptions);
  }

  addBooking(booking: Booking): Observable<Booking> {
    const tkn = this.authService.getToken();
    if (tkn === null) {
      console.log('Token is null');
      return throwError('Token is null');
    }
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    console.log('here in all service' + httpOptions);
    console.log(httpOptions.headers.get('Authorization'));
    console.log(this.authService.isTokenExpired(tkn));
    return this.http
      .post<Booking>(this.baseUrlForBooking + '/add', booking, httpOptions)
      .pipe(catchError(this.errorHandler));
  }

  showAllBookingsByEmail(email: string): Observable<Binded[]> {
    const tkn = this.authService.getToken();

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    const url = `${this.baseUrlForBooking}/getByBookingEmail/${email}`;
    return this.http.get<Binded[]>(url, httpOptions);
  }
  showAllBookingsForAdmin(): Observable<Booking[]> {
    const tkn = this.authService.getToken();

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    const url = `${this.baseUrlForBooking}/all`;
    return this.http.get<Booking[]>(url, httpOptions);
  }
  deletePassenger(passengerId: number): Observable<string> {
    const tkn = this.authService.getToken();

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + tkn,
      }),
    };
    const url = `${this.baseUrlForBooking}/cancelBooking/${passengerId}`;
    return this.http.delete<string>(url, httpOptions);
  }

  createBookingTransaction(
    booking: Booking,
    amount: string
  ): Observable<TransactionDetails> {
    const n = parseInt(amount);
    const val = this.http.post<TransactionDetails>(
      'http://localhost:8087/createTransaction/' + n,
      booking
    );
    console.log('__________');
    console.log(val);
    console.log(booking, amount);
    console.log('__________');
    return val;
  }

  errorHandler(error: any) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      // Get client-side error
      errorMessage = error.error.message;
    } else {
      // Get server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    console.log(errorMessage);
    return throwError(() => error);
  }

  sendContactForm(contact: ContactUs): Observable<any> {
    const token = this.authService.getToken();
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + token,
      }),
    };
    return this.http.post<any>(this.apiUrl, contact, httpOptions);
  }
}
