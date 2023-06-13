import { CinemaHall } from './cinema-hall';
import { Time } from '@angular/common';

export interface Screening {
  id: number;
  movie: Movie;
  cinemaHall: CinemaHall;
  date: string;
  startTime: string;
  endTime: string;
}

export interface CreateScreeningPayload {
  movieId: number;
  cinemaHallId: number;
  date: string;
  startTime: string;
}

export interface Movie {
  id: number;
  imageUrl: string;
  title: string;
  description: string;
  genre: string;
  runtimeMinutes: number;
}
