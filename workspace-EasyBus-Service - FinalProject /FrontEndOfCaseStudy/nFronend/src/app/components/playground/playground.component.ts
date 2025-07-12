import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-playground',
  standalone: true,
  imports: [],
  templateUrl: './playground.component.html',
  styleUrl: './playground.component.css',
})
export class PlaygroundComponent {
  constructor(private authService: AuthService, private router: Router) {}
  logoutHandler() {
    this.authService.clearToken();
    this.router.navigateByUrl('/login');
  }
}
