import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Movie } from '../../common/model/screening';

const MOVIE_ENDPOINT = '/api/movies';

@Injectable({
  providedIn: 'root',
})
export class MovieService {
  constructor(private httpClient: HttpClient) {}

  getMovies() {
    return this.httpClient.get<Movie[]>(MOVIE_ENDPOINT);
  }

  deleteMovie(movieId: number) {
    this.httpClient.delete<Movie>(MOVIE_ENDPOINT + '/' + movieId);
  }
  addMovie(m: Movie) {
    return this.httpClient.post<Movie>(MOVIE_ENDPOINT, m);
  }
}
