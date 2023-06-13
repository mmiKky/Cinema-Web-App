import { Component } from '@angular/core';
import { OrderService } from '../common/services/order.service';
import { Observable, withLatestFrom } from 'rxjs';
import { Order } from '../common/model/order';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { UserService } from '../common/services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss'],
})
export class CheckoutComponent {
  order$: Observable<Order | null>;
  paymentData = new FormGroup(
    {
      email: new FormControl('', [Validators.email, Validators.required]),
      emailConfirmation: new FormControl('', [
        Validators.email,
        Validators.required,
      ]),
      name: new FormControl('', [Validators.required]),
      acceptance: new FormControl(false, [Validators.requiredTrue]),
    },
    CustomValidators.matchingEmails
  );

  constructor(
    private orderService: OrderService,
    private userService: UserService,
    private router: Router
  ) {
    this.order$ = orderService.retrieveOrder();
    userService.getUser().subscribe((user) => {
      if (user !== null) {
        this.paymentData.controls['email'].setValue(user.email);
        this.paymentData.controls['name'].setValue(
          user.name + ' ' + user.surname
        );
        this.paymentData.controls.emailConfirmation.setValue(user.email);
      }
    });
  }

  getCurrentTotal() {
    return this.orderService.getCurrentTotal();
  }

  goBack() {
    this.order$.subscribe((order) => {
      this.router.navigate(['screening', order?.screening?.id]);
    });
  }

  reserve() {
    this.order$
      .pipe(withLatestFrom(this.userService.getUser()))
      .subscribe(([order, user]) => {
        this.orderService.reserve(
          order?.selectedSeats!,
          user!,
          order?.screening
        );
      });

    this.router.navigate(['']);
  }
}

class CustomValidators {
  static matchingEmails: ValidatorFn = (
    group: AbstractControl
  ): ValidationErrors | null => {
    let pass = group.get('email')?.value;
    let confirmPass = group.get('emailConfirmation')?.value;
    return pass === confirmPass ? null : { notSame: true };
  };
}
