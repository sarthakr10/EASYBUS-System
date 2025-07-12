import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowAllBookingsComponent } from './show-all-bookings.component';

describe('ShowAllBookingsComponent', () => {
  let component: ShowAllBookingsComponent;
  let fixture: ComponentFixture<ShowAllBookingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowAllBookingsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ShowAllBookingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
