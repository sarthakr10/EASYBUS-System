import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, retryWhen, throwError } from 'rxjs';
import UserLogin from '../models/user-login';
import { FormGroup } from '@angular/forms';
import { UserRegistration } from '../models/user-registration';
import { randomUUID } from 'node:crypto';

@Injectable({
  providedIn: 'root',
})
export class ConnectorService {
  private apiUrl = 'http://localhost:8086/';

  constructor(private httpClient: HttpClient) {}

  doLogin(login: UserLogin): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
    };
    console.log('here 6');
    const data = {
      email: login.emailId,
      password: login.password,
    };
    console.log(data);
    return this.httpClient
      .post<any>(this.apiUrl + 'api/user/login', data, httpOptions)
      .pipe(catchError(this.errorHandler));
  }

  doSignUp(registerObj: UserRegistration) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
    };
    const data = {
      name: registerObj.username,
      email: registerObj.emailId,
      password: registerObj.password,
      phone: registerObj.phone,
      role: 'USER',
    };
    console.log('here');
    return this.httpClient
      .post(this.apiUrl + 'api/user/register', data, httpOptions)
      .pipe(catchError(this.errorHandler));
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
}
