import { Component, OnInit } from '@angular/core';

import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-wild-card-redirects',
  standalone: true,
  imports: [],
  templateUrl: './wild-card-redirects.component.html',
  styleUrl: './wild-card-redirects.component.css',
})
export class WildCardRedirectsComponent implements OnInit {
  constructor(private authService: AuthService, private router: Router) {}
  ngOnInit(): void {
    const token = this.authService.getToken();
    if (!token) {
      // No token or expired token
      this.router.navigateByUrl('/playground');
    } else {
      const role = this.authService.getUserRole();
      if (role === 'ROLE_ADMIN') {
        this.router.navigateByUrl('/dashboard');
      } else if (role === 'ROLE_USER') {
        this.router.navigateByUrl('/playground');
      } else {
        this.router.navigateByUrl('/playground');
      }
    }
  }
}
