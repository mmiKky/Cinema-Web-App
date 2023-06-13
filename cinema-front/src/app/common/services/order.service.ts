import { Injectable } from '@angular/core';
import { Seat } from '../model/cinema-hall';
import { HttpClient } from '@angular/common/http';
import { Order } from '../model/order';
import { BehaviorSubject } from 'rxjs';
import { ScreeningService } from '../../welcome-page/repertoire/screening.service';
import { Reservation, User } from '../model/user';
import { Screening } from '../model/screening';

const ORDER_ENDPOINT = '/api/reservations';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private order: Order;
  order$: BehaviorSubject<Order | null> = new BehaviorSubject<Order | null>(
    null
  );

  constructor(
    private http: HttpClient,
    private screeningService: ScreeningService
  ) {
    this.order = { selectedSeats: [] };
  }

  retrieveOrder() {
    return this.order$.asObservable();
  }

  createOrder(screeningId: string, user: User) {
    this.screeningService
      .getScreeningById(screeningId)
      .subscribe((screening) => {
        this.order = { screening, selectedSeats: [], user };
        this.order$.next(this.order);
      });
    return this.order$.asObservable();
  }

  selectSeat(seat: Seat) {
    if (seat.available) {
      let ind = this.order.selectedSeats.indexOf(seat);
      if (ind !== -1) {
        this.order.selectedSeats.splice(ind, 1);
      } else {
        this.order.selectedSeats.push(seat);
      }
      this.order$.next(this.order);
    }
  }

  getSelectedSeatsSummary(): string {
    return this.order.selectedSeats.length > 0
      ? this.order.selectedSeats.reduce((summary, seat) => {
          return summary + seat.row + '-' + seat.seatNr + ', ';
        }, '')
      : ' ';
  }

  getCurrentTotal(): number {
    return this.order.selectedSeats.length > 0
      ? this.order.selectedSeats.reduce((sum, seat) => {
          return sum + seat.price;
        }, 0)
      : 0;
  }

  isSelectedSeat(seatArrayElement: Seat) {
    return this.order.selectedSeats.includes(seatArrayElement);
  }

  getReservationsForUser(user: User) {
    return this.http.get<Reservation[]>(ORDER_ENDPOINT + '/user/' + user.id);
  }

  reserve(selectedSeats: Seat[], user?: User, screening?: Screening) {
    for (let seat of selectedSeats) {
      this.http
        .post<Reservation>(ORDER_ENDPOINT, {
          userId: user?.id,
          screeningId: screening?.id,
          seatRow: seat.row,
          seatNr: seat.seatNr,
          price: seat.price,
        })
        .subscribe();
    }
  }

  cancelReservation(reservationId: string) {
    this.http.delete(ORDER_ENDPOINT + '/' + reservationId);
  }
}
