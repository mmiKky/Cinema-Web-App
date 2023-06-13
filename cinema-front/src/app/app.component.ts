import { Component } from '@angular/core';
import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  title = 'cinema-front';
  url: string = '';

  constructor(
    iconRegistry: MatIconRegistry,
    sanitizer: DomSanitizer,
    route: ActivatedRoute
  ) {
    iconRegistry.addSvgIcon(
      'popcorn',
      sanitizer.bypassSecurityTrustResourceUrl('./assets/cinema.svg')
    );
    route.url.subscribe((url) => (this.url = url.toString()));
  }

  goBack() {}
}
