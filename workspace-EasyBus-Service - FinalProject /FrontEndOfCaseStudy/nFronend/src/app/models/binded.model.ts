import { Booking } from '../components/booking/booking.model';
import Bus from '../components/bus/bus.model';

export default interface Binded {
  booking: Booking;
  bus: Bus;
}
