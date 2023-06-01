package com.cinema.cinemawebapp.movie;

import com.cinema.cinemawebapp.movie.models.Movie;
import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, Integer> {
}
