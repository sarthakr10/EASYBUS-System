import { Passenger } from '../../models/Passenger.model';

export interface Booking {
  bookingId: number;
  busId: number;
  noOfBookings: number;
  typeSleeperCapacity: number;
  typeSittingCapacity: number;
  passengerList?: Passenger[];
  bookingEmail: string;
  refund: number;
  userName: string;
}
