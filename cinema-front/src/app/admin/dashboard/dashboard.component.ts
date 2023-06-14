import {
  ChangeDetectionStrategy,
  Component,
  Inject,
  OnDestroy,
  OnInit,
  ViewEncapsulation,
} from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MovieService } from '../services/movie.service';
import { Observable } from 'rxjs';
import { Movie, Screening } from '../../common/model/screening';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmationDialogComponent } from '../../common/components/confirmation-dialog/confirmation-dialog.component';
import { ScreeningService } from '../../welcome-page/repertoire/screening.service';
import { CinemaHall } from '../../common/model/cinema-hall';
import { CinemaHallService } from '../../common/services/cinema-hall.service';
import { dateToString } from '../../common/utils/utils';
import { CalendarDateFormatter, CalendarEvent } from 'angular-calendar';
import { MatSnackBar } from '@angular/material/snack-bar';
import * as dayjs from 'dayjs';
import { DOCUMENT } from '@angular/common';
import tippy from 'tippy.js';

enum GENRE_LIST {
  'HORROR',
  'ADVENTURE',
  'ANIMATION',
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DashboardComponent implements OnInit, OnDestroy {
  fb = new FormBuilder();
  movieForm = this.fb.nonNullable.group({
    title: new FormControl('', [Validators.required]),
    runtime: new FormControl(0, [Validators.required]),
    description: new FormControl(''),
    genre: new FormControl(''),
    imageUrl: new FormControl('', [Validators.required]),
  });
  screeningForm = this.fb.nonNullable.group({
    movie: new FormControl(0),
    date: new FormControl(new Date()),
    startTime: new FormControl(''),
    cinemaHall: new FormControl(0),
  });
  movies$: Observable<Movie[]>;
  genres = [GENRE_LIST.ADVENTURE, GENRE_LIST.ANIMATION, GENRE_LIST.HORROR];
  editing = false;
  screenings$: Observable<Screening[]>;
  cinemaHalls$: Observable<CinemaHall[]>;
  constructor(
    private movieService: MovieService,
    private screeningService: ScreeningService,
    private cinemaHallService: CinemaHallService,
    public dialog: MatDialog,
    private snackbar: MatSnackBar,
    @Inject(DOCUMENT) private document: Document
  ) {
    this.movies$ = movieService.getMovies();
    this.screenings$ = screeningService.getAllScreenings();
    this.cinemaHalls$ = cinemaHallService.getAllCinemaHalls();
  }

  addMovie() {
    this.movieService
      .addMovie({
        title: this.movieForm.controls.title.value!,
        runtimeMinutes: this.movieForm.controls.runtime.value!,
        description: this.movieForm.controls.description.value ?? '',
        genre: this.movieForm.controls.genre.value ?? '',
        imageUrl: this.movieForm.controls.imageUrl.value!,
      } as Movie)
      .subscribe(() => {
        this.movieForm.reset();
        this.movies$ = this.movieService.getMovies();
      });
  }

  deleteMovie(movieId: number) {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: {},
    });
    dialogRef.afterClosed().subscribe((result: boolean) => {
      if (result) {
        this.movieService.deleteMovie(movieId);
        this.movies$ = this.movieService.getMovies();
      }
    });
  }

  editMovie(movie: Movie) {
    this.editing = true;
  }

  addScreening() {
    this.screeningService
      .addScreening({
        movieId: this.screeningForm.controls.movie.value!,
        cinemaHallId: this.screeningForm.controls.cinemaHall.value!,
        date: dateToString(this.screeningForm.controls.date.value!),
        startTime: this.screeningForm.controls.startTime.value!,
      })
      .subscribe(() => {
        this.screeningForm.reset();
        this.snackbar.open('Successfully added screening');
        this.screenings$ = this.screeningService.getAllScreenings();
      });
  }

  editScreening(screening: Screening) {}

  deleteScreening(screening: Screening) {
    this.screeningService.deleteScreening(screening.id).subscribe(() => {
      this.screenings$ = this.screeningService.getAllScreenings();
    });
  }

  mapScreeningToCalendarEvent(screenings: Screening[] | null): CalendarEvent[] {
    return (
      screenings?.map((screening) => {
        const startDate = dayjs(screening.date)
          .set('h', Number(screening.startTime.split(':')[0]))
          .set('m', Number(screening.startTime.split(':')[1]))
          .toDate();

        return {
          start: startDate,
          title: screening.movie.title,
          end: dayjs(startDate)
            .add(screening.movie.runtimeMinutes, 'm')
            .toDate(),
          meta: { screening },
        };
      }) ?? []
    );
  }

  ngOnInit(): void {
    this.document.body.classList.add('dark-theme');
  }

  ngOnDestroy(): void {
    this.document.body.classList.remove('dark-theme');
  }

  handleEventClicked($event: {
    event: CalendarEvent;
    sourceEvent: MouseEvent | KeyboardEvent;
  }): void {
    console.log('bonk');
    const dialogRef = this.dialog.open(ConfirmationDialogComponent);
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.deleteScreening($event.event.meta.screening);
      }
    });
  }

  protected readonly GENRE_LIST = GENRE_LIST;
  viewDate = new Date();
}
