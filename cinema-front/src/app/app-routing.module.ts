import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { WelcomePageComponent } from './welcome-page/welcome-page.component';
import { SeatPickerComponent } from './seat-picker/seat-picker.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { activeOrderGuard } from './active-order.guard';
import { DashboardComponent } from './admin/dashboard/dashboard.component';

const routes: Routes = [
  { path: '', component: WelcomePageComponent },
  { path: 'screening/:screeningId', component: SeatPickerComponent },
  {
    path: 'checkout',
    component: CheckoutComponent,
    canActivate: [activeOrderGuard],
  },
  {
    path: 'admin',
    children: [{ path: 'dashboard', component: DashboardComponent }],
  },
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
