import { Injectable } from '@angular/core';
import { BreakpointObserver } from '@angular/cdk/layout';
import { map } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LayoutManagerService {
  constructor(private breakpointObserver: BreakpointObserver) {}

  isPortrait$ = this.breakpointObserver
    .observe(['(orientation: portrait)'])
    .pipe(map((result) => result.matches));
}
