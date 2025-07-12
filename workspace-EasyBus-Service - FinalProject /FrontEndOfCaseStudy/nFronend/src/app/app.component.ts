import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { AuthService } from './services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent implements OnInit {
  navbarInit() {
    if (sessionStorage.getItem('jwtToken')) {
      this.username = this.authService.getUserName();
      return true;
    }
    return false;
  }
  isNavBarVisible = true;
  logout() {
    this.authService.clearToken();
    // this.username = '';
    this.token = '';
    this.router.navigateByUrl('/login');
  }
  title = 'frontend';
  token: string | null = '';
  username: string | null = '';
  constructor(private authService: AuthService, private router: Router) {}
  ngOnInit(): void {
    this.token = this.authService.getToken();
    if (this.token == null) {
      this.router.navigateByUrl('/');
    } else {
      console.log();
      this.username = this.authService.getUserName();
      const userRole = this.authService.getUserRole();
      if (userRole === 'ADMIN') {
        this.isNavBarVisible = false;
        this.router.navigateByUrl('/admin');
      } else if (userRole === 'USER') {
        this.router.navigateByUrl('/home');
      }
    }
  }
}
