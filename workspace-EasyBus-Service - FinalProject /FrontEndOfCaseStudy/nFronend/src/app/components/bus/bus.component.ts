import { Component, OnInit } from '@angular/core';
import Bus from './bus.model';
import { AllServiceService } from '../../services/all-service.service';
import { DataService } from '../../services/data.service';
import { CommonModule } from '@angular/common';

import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-bus',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './bus.component.html',
  styleUrl: './bus.component.css',
})
export class BusComponent implements OnInit {
  bus: Bus[] = [];
  selectedFilter: string = 'default';
  constructor(
    private allService: AllServiceService,
    private dataService: DataService,
    private router: Router
  ) {}
  ngOnInit(): void {
    // this.bus = busses;
    const { source, destination, date } =
      this.dataService.getSourceDestinationDate();
    this.allService
      .getBusByRouteAndDate(source, destination, date)
      .subscribe((val) => {
        this.bus = val;
        console.log(this.bus);
      });
  }

  bookBus(bus: Bus, event: Event) {
    event.preventDefault();
    this.dataService.setBus(bus);
    this.router.navigateByUrl('/booking');
  }

  sortBuses() {
    switch (this.selectedFilter) {
      case 'fastest':
        this.bus.sort((a, b) => a.busDuration - b.busDuration);
        break;
      case 'cheapest':
        this.bus.sort((a, b) => a.priceSitting - b.priceSitting);
        break;
      case 'early':
        this.bus.sort((a, b) => {
          const timeA = new Date(`1970/01/01 ${a.departureFromSource}`);
          const timeB = new Date(`1970/01/01 ${b.departureFromSource}`);
          return timeA.getTime() - timeB.getTime();
        });
        break;
      case 'late':
        this.bus.sort((a, b) => {
          const timeA = new Date(`1970/01/01 ${a.departureFromSource}`);
          const timeB = new Date(`1970/01/01 ${b.departureFromSource}`);
          return timeB.getTime() - timeA.getTime();
        });
        break;
      case 'default':
      default:
        this.bus.sort((a, b) => a.busId - b.busId);
        break;
    }
  }
}
