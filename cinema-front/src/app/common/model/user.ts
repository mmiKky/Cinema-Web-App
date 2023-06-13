import { Seat } from './cinema-hall';

export interface User {
  id: number;
  name: string;
  surname: string;
  email: string;
}

export interface Reservation {
  seats: Seat[];
  userName: string;
  userSurname: string;
  userEmail: string;
  screeningDate: string;
  screeningStartTime: string;
  screeningEndTime: string;
  movieId: number;
  movieTitle: string;
  cinemaHallName: string;
}
