import { Injectable } from '@angular/core';
import {
  CreateScreeningPayload,
  Screening,
} from '../../common/model/screening';
import { dateToString } from '../../common/utils/utils';
import { HttpClient } from '@angular/common/http';

const SCREENING_ENDPOINT = '/api/screenings';

@Injectable({
  providedIn: 'root',
})
export class ScreeningService {
  constructor(private httpClient: HttpClient) {}

  getScreeningsByDate(date: Date) {
    return this.httpClient.get<Screening[]>(
      SCREENING_ENDPOINT + '/date/' + dateToString(date)
    );
  }

  getScreeningById(id: string) {
    return this.httpClient.get<Screening>(SCREENING_ENDPOINT + '/' + id);
  }

  getAllScreenings() {
    return this.httpClient.get<Screening[]>(SCREENING_ENDPOINT);
  }

  addScreening(s: CreateScreeningPayload) {
    return this.httpClient.post(SCREENING_ENDPOINT, s);
  }
  deleteScreening(screeningId: number) {
    return this.httpClient.delete(SCREENING_ENDPOINT + '/' + screeningId);
  }
}
