import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ScreeningService } from './screening.service';
import { Movie, Screening } from '../../common/model/screening';
import { dateToString } from '../../common/utils/utils';
import { BehaviorSubject, mergeMap, Observable, Subject, take } from 'rxjs';

@Component({
  selector: 'app-repertoire',
  templateUrl: './repertoire.component.html',
  styleUrls: ['./repertoire.component.scss'],
})
export class RepertoireComponent {
  repertoire: { movie: Movie; screenings: Screening[] }[] = [];
  chosenDate: Subject<Date> = new BehaviorSubject(new Date());
  chosenDate$: Observable<Date> = this.chosenDate.asObservable();
  constructor(
    private router: Router,
    private screeningService: ScreeningService
  ) {
    this.chosenDate$
      .pipe(
        mergeMap((currentDate) =>
          screeningService.getScreeningsByDate(currentDate)
        )
      )
      .subscribe((screenings) => {
        this.repertoire = [];
        for (let screening of screenings) {
          if (this.repertoire.length === 0) {
            this.repertoire.push({
              movie: screening.movie,
              screenings: [screening],
            });
          } else {
            let added = false;
            for (let entry of this.repertoire) {
              if (entry.movie.id === screening.movie.id) {
                entry.screenings.push(screening);
                added = true;
              }
            }
            if (!added) {
              this.repertoire.push({
                movie: screening.movie,
                screenings: [screening],
              });
            }
          }
        }
      });
  }

  handleClick(screeningId: number) {
    this.router.navigate(['screening', screeningId]);
  }

  protected readonly dateToString = dateToString;

  incrementDate() {
    this.chosenDate$.pipe(take(1)).subscribe((val) => {
      const nextDate = new Date(+val);
      nextDate.setDate(nextDate.getDate() + 1);
      this.chosenDate.next(nextDate);
    });
  }

  decrementDate() {
    this.chosenDate$.pipe(take(1)).subscribe((val) => {
      const nextDate = new Date(+val);
      nextDate.setDate(nextDate.getDate() - 1);
      this.chosenDate.next(nextDate);
    });
  }
}
