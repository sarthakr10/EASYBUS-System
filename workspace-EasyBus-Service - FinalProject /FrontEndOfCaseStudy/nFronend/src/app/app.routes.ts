import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { WildCardRedirectsComponent } from './components/wild-card-redirects/wild-card-redirects.component';
import { PlaygroundComponent } from './components/playground/playground.component';
import { RegisterComponent } from './components/register/register.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import { BusComponent } from './components/bus/bus.component';
import { BookingComponent } from './components/booking/booking.component';
import { PaymentComponent } from './components/payment/payment.component';
import { ShowAllBookingsComponent } from './show-all-bookings/show-all-bookings.component';
import { AdminComponent } from './components/admin/admin.component';
import { ContactUsComponent } from './components/contact-us/contact-us.component';
import { AboutUsComponent } from './components/about-us/about-us.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomepageComponent },
  { path: 'signup', component: RegisterComponent },
  { path: 'bus', component: BusComponent },
  { path: 'payment', component: PaymentComponent },
  {
    path: 'showAll',
    component: ShowAllBookingsComponent,
  },
  { path: 'booking', component: BookingComponent },
  { path: 'admin', component: AdminComponent },
  { path: 'contact', component: ContactUsComponent },
  { path: 'about', component: AboutUsComponent },
  { path: 'playground', component: PlaygroundComponent },
  // { path: '**', component: WildCardRedirectsComponent },
];
