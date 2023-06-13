import { Injectable } from '@angular/core';
import { map, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { CinemaHall } from '../model/cinema-hall';

const CINEMAHALL_ENDPOINT = '/api/cinema-hall';

@Injectable({
  providedIn: 'root',
})
export class CinemaHallService {
  constructor(private httpClient: HttpClient) {}

  getSeatsForCinemaHall(screeningId: number) {
    return this.getCinemaHall(screeningId).pipe(
      map((cinemaHall) => cinemaHall.seatsList)
    );
  }

  getCinemaHall(screeningId: number) {
    return this.httpClient.get<CinemaHall>(
      CINEMAHALL_ENDPOINT + '/' + screeningId
    );
  }

  getAllCinemaHalls() {
    return this.httpClient.get<CinemaHall[]>(CINEMAHALL_ENDPOINT);
  }
}
