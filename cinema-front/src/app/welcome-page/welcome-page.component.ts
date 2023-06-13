import { Component } from '@angular/core';
import { LayoutManagerService } from '../common/services/layout-manager.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-welcome-page',
  templateUrl: './welcome-page.component.html',
  styleUrls: ['./welcome-page.component.scss'],
})
export class WelcomePageComponent {
  isPortrait$: Observable<boolean>;

  constructor(private layoutManager: LayoutManagerService) {
    this.isPortrait$ = layoutManager.isPortrait$;
  }
}
