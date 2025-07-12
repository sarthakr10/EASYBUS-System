import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})

//use session storage
export class AuthService {
  private readonly jwtTokenKey = 'jwtToken';
  constructor() {}

  setToken(token: string): void {
    sessionStorage.setItem(this.jwtTokenKey, token);
  }

  getToken(): string {
    let t: string = '';
    const token = sessionStorage.getItem(this.jwtTokenKey);
    if (token != null) {
      t = token;
    }
    if (token && this.isTokenExpired(token)) {
      this.clearToken();
      return '';
    }
    return t;
  }
  clearToken(): void {
    sessionStorage.removeItem(this.jwtTokenKey);
  }
  isTokenExpired(token: string): boolean {
    if (!token) {
      return true;
    }
    const tokenPayload = JSON.parse(atob(token.split('.')[1]));
    const expiry = new Date(tokenPayload.exp * 1000);
    return expiry <= new Date();
  }
  getUserRole(): string | null {
    const token = this.getToken();
    if (!token) {
      return null;
    }
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.role;
  }

  getUserEmail(): string | null {
    const token = this.getToken();
    if (!token) {
      return null;
    }
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.email;
  }

  getUserName(): string | null {
    const token = this.getToken();
    if (!token) {
      return null;
    }
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.sub;
  }
}
