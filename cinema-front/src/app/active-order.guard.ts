import { OrderService } from './common/services/order.service';
import { inject } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { Router } from '@angular/router';

export const activeOrderGuard = () => {
  const orderService = inject(OrderService);
  const router = inject(Router);

  return orderService.retrieveOrder().pipe(
    map((order) => {
      console.log(order);
      if (order !== null) {
        return of(true);
      }
      router.navigate(['']);
      return of(false);
    }),
    catchError(() => {
      router.navigate(['']);
      return of(false);
    })
  );
};
