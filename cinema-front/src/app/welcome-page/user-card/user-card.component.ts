import { Component } from '@angular/core';
import { UserService } from '../../common/services/user.service';
import { filter, mergeMap, Observable, tap } from 'rxjs';
import { Reservation, User } from '../../common/model/user';
import { OrderService } from '../../common/services/order.service';

@Component({
  selector: 'app-user-card',
  templateUrl: './user-card.component.html',
  styleUrls: ['./user-card.component.scss'],
})
export class UserCardComponent {
  isUserLoggedIn: boolean = false;
  showRegisterForm: boolean = true;
  isLoading: boolean = false;
  user$: Observable<User | null>;
  reservations$: Observable<Reservation[]>;

  constructor(
    private userService: UserService,
    private orderService: OrderService
  ) {
    this.user$ = userService.getUser().pipe(
      tap((user) => {
        if (user !== null) {
          this.isUserLoggedIn = true;
          this.isLoading = false;
        }
      })
    );
    this.reservations$ = this.user$.pipe(
      filter((user) => user !== null),
      mergeMap((user) => this.orderService.getReservationsForUser(user!))
    );
  }

  toggleForm() {
    this.showRegisterForm = !this.showRegisterForm;
  }

  toggleLoading() {
    this.isLoading = true;
  }

  logout() {
    this.userService.logout();
    this.isUserLoggedIn = false;
  }

  cancelReservation(reservation: Reservation) {
    // this.orderService.cancelReservation(reservation);
  }
}
