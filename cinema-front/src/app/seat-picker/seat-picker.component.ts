import { Component, OnDestroy, OnInit } from '@angular/core';
import { Seat } from '../common/model/cinema-hall';
import { Observable, Subscription, tap } from 'rxjs';
import { Router } from '@angular/router';
import { OrderService } from '../common/services/order.service';
import { Order } from '../common/model/order';
import { User } from '../common/model/user';
import { CinemaHallService } from '../common/services/cinema-hall.service';
import { LayoutManagerService } from '../common/services/layout-manager.service';

@Component({
  selector: 'app-seat-picker',
  templateUrl: './seat-picker.component.html',
  styleUrls: ['./seat-picker.component.scss'],
})
export class SeatPickerComponent implements OnDestroy {
  readonly cinemaHallDimensions = [...Array(25).keys()];
  readonly cinemaHallRows = [...Array(12).keys()];
  seatArrayFilled = false;
  isPortrait$: Observable<boolean>;
  selectSeat(s: Seat) {
    this.orderService.selectSeat(s);
  }
  isSelectedSeat(s: Seat) {
    return this.orderService.isSelectedSeat(s);
  }
  getSelectedSeatsSummary() {
    return this.orderService.getSelectedSeatsSummary();
  }
  getCurrentTotal() {
    return this.orderService.getCurrentTotal();
  }

  order$: Observable<Order | null>;
  seatArray: Seat[][];

  subscriptions: Subscription[] = [];

  constructor(
    private orderService: OrderService,
    private cinemaHallService: CinemaHallService,
    private router: Router,
    private layoutManager: LayoutManagerService
  ) {
    const array = Array(this.cinemaHallRows.length);
    for (let i = 0; i < this.cinemaHallRows.length; i++) {
      array[i] = Array(this.cinemaHallDimensions.length);
    }
    this.seatArray = array;
    this.order$ = this.orderService
      .createOrder(this.router.url.split('/')[2], {} as User)
      .pipe(
        tap((order) => {
          if (order !== null && order.screening && !this.seatArrayFilled) {
            this.cinemaHallService
              .getSeatsForCinemaHall(order!.screening.id)
              .subscribe((seats) => {
                this.seatArrayFilled = true;
                seats.map((seat) => {
                  this.seatArray[seat.row][seat.seatNr] = seat;
                });
              });
          }
        })
      );
    this.isPortrait$ = layoutManager.isPortrait$;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub) => sub.unsubscribe());
  }

  goBack() {
    this.router.navigate(['/']);
  }

  goToCheckout() {
    this.router.navigate(['checkout']);
  }
}
