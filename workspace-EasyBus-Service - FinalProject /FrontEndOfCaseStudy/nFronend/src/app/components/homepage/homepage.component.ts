import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AllServiceService } from '../../services/all-service.service';
import Bus from '../bus/bus.model';
import { DataService } from '../../services/data.service';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-homepage',
  standalone: true,
  imports: [RouterLink, CommonModule, FormsModule],
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.css',
})
export class HomepageComponent implements OnInit {
  tempSourceContainer: string[] = [];
  tempDestinationContainer: string[] = [];

  destinationVal: string = '';

  sourceVal: string = '';
  sources: string[] = [];
  destinations: string[] = [];

  bus: Bus[] = [];
  ngOnInit(): void {}
  constructor(
    private dataService: DataService,
    private router: Router,
    private authService: AuthService,
    private allService: AllServiceService
  ) {
    if (
      authService.getToken()?.length === 0 ||
      authService.getToken() === null
    ) {
      this.router.navigateByUrl('/login');
    }
    allService.getDestinations().subscribe((val) => (this.destinations = val));
    allService.getSources().subscribe((val) => (this.sources = val));
  }
  search(source: string, destination: string, date: string, event: Event) {
    event.preventDefault();
    console.log(source, destination, date);
    this.dataService.setSourceDestinationDate(source, destination, date);
    this.router.navigateByUrl('/bus');
  }

  nameHandler(flag: number) {
    if (flag === 1) {
      this.tempSourceContainer = [];
      for (let n of this.sources) {
        if (
          this.sourceVal.length > 0 &&
          n.substring(0, this.sourceVal.length).toLowerCase() ===
            this.sourceVal.toLowerCase()
        ) {
          this.tempSourceContainer.push(n);
        }
      }
    } else {
      this.tempDestinationContainer = [];
      for (let n of this.destinations) {
        if (
          this.destinationVal.length > 0 &&
          n.substring(0, this.destinationVal.length).toLowerCase() ===
            this.destinationVal.toLowerCase()
        ) {
          this.tempDestinationContainer.push(n);
        }
      }
    }
  }
  destinationHandler(name: string) {
    this.destinationVal = name;
    this.tempDestinationContainer = [];
  }
  sourceHandler(name: string) {
    this.sourceVal = name;
    this.tempSourceContainer = [];
  }
}
