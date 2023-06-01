package com.cinema.cinemawebapp.movie;

import com.cinema.cinemawebapp.exceptions.MovieNotFoundException;
import com.cinema.cinemawebapp.movie.models.Movie;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie){
        return ResponseEntity.ok(movieRepository.save(movie));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable int id) throws MovieNotFoundException {
        Movie foundMovie = movieRepository.findById(id).orElseThrow(MovieNotFoundException::new);

        return ResponseEntity.ok(foundMovie);
    }

    @GetMapping
    public ResponseEntity<Iterable<Movie>> getMovies(){
        return ResponseEntity.ok(movieRepository.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Movie> deleteMovie(@PathVariable int id){
        movieRepository.deleteById(id);

        return ResponseEntity.ok(null);
    }
}
