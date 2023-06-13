import { Screening } from './screening';
import { Seat } from './cinema-hall';
import { User } from './user';

export interface Order {
  screening?: Screening;
  selectedSeats: Seat[];
  user?: User;
}
