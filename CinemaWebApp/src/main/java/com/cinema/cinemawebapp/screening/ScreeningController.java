package com.cinema.cinemawebapp.screening;

import com.cinema.cinemawebapp.cinemahall.CinemaHallController;
import com.cinema.cinemawebapp.cinemahall.CinemaHallRepository;
import com.cinema.cinemawebapp.cinemahall.models.CinemaHall;
import com.cinema.cinemawebapp.cinemahall.models.CinemaHallResponse;
import com.cinema.cinemawebapp.exceptions.CinemaHallNotFoundException;
import com.cinema.cinemawebapp.exceptions.MovieNotFoundException;
import com.cinema.cinemawebapp.exceptions.ScreeningNotFoundException;
import com.cinema.cinemawebapp.movie.MovieRepository;
import com.cinema.cinemawebapp.movie.models.Movie;
import com.cinema.cinemawebapp.reservation.models.Seat;
import com.cinema.cinemawebapp.screening.models.Screening;
import com.cinema.cinemawebapp.screening.models.ScreeningResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/screenings")
public class ScreeningController {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final CinemaHallRepository cinemaHallRepository;

    public ScreeningController(ScreeningRepository screeningRepository, MovieRepository movieRepository, CinemaHallRepository cinemaHallRepository) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.cinemaHallRepository = cinemaHallRepository;
    }

    @GetMapping
    public ResponseEntity<Iterable<ScreeningResponse>> getAllScreenings(){
        List<ScreeningResponse> list_sr = new ArrayList<>();
        for(Screening s : screeningRepository.findAll()){
            ScreeningResponse sr = new ScreeningResponse();
            sr.setId(s.getId());
            sr.setMovie(movieRepository.findById(s.getMovieId()).orElseThrow());
            sr.setCinemaHall(cinemaHallRepository.findById(s.getCinemaHallId()).orElseThrow());
            sr.setDate(s.getDate());
            sr.setStartTime(s.getStartTime());
            sr.setEndTime(s.getEndTime());
            list_sr.add(sr);
        }
        return ResponseEntity.ok(list_sr);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<Iterable<ScreeningResponse>> getScreeningsByDate(@PathVariable String date){
        List<ScreeningResponse> list_sr = new ArrayList<>();
        for(Screening s : screeningRepository.findAll()){
            if(s.getDate().equals(date)) {
                ScreeningResponse sr = new ScreeningResponse();
                sr.setId(s.getId());
                sr.setMovie(movieRepository.findById(s.getMovieId()).orElseThrow());
                sr.setCinemaHall(cinemaHallRepository.findById(s.getCinemaHallId()).orElseThrow());
                sr.setDate(s.getDate());
                sr.setStartTime(s.getStartTime());
                sr.setEndTime(s.getEndTime());
                list_sr.add(sr);
            }
        }
        return ResponseEntity.ok(list_sr);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScreeningResponse> getScreening(@PathVariable int id) throws ScreeningNotFoundException {
        Screening foundScreening = screeningRepository.findById(id).orElseThrow(ScreeningNotFoundException::new);
        ScreeningResponse sr = new ScreeningResponse();
        sr.setId(foundScreening.getId());
        sr.setMovie(movieRepository.findById(foundScreening.getMovieId()).orElseThrow());
        sr.setCinemaHall(cinemaHallRepository.findById(foundScreening.getCinemaHallId()).orElseThrow());
        sr.setDate(foundScreening.getDate());
        sr.setStartTime(foundScreening.getStartTime());
        sr.setEndTime(foundScreening.getEndTime());
        return ResponseEntity.ok(sr);
    }

    @PostMapping
    public ResponseEntity<Screening> addScreening(@RequestBody Screening newScreening) throws MovieNotFoundException, CinemaHallNotFoundException {
        validateNewScreening(newScreening);

        return ResponseEntity.ok(screeningRepository.save(newScreening));
    }

    @PutMapping
    public ResponseEntity<Screening> updateScreening(@RequestBody Screening updatedScreening) throws ScreeningNotFoundException, CinemaHallNotFoundException, MovieNotFoundException {
        validateNewScreening(updatedScreening);

        Optional<Screening> foundScreening = screeningRepository.findById(updatedScreening.getId());

        if (foundScreening.isPresent())
            return ResponseEntity.ok(screeningRepository.save(updatedScreening));
        else
            throw new ScreeningNotFoundException();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Screening> deleteScreening(@PathVariable int id){
        screeningRepository.deleteById(id);

        return ResponseEntity.ok(null);
    }

    private void validateNewScreening(Screening screening) throws MovieNotFoundException, CinemaHallNotFoundException {
        if(movieRepository.findById(screening.getMovieId()).isEmpty())
            throw new MovieNotFoundException();
        if(cinemaHallRepository.findById(screening.getCinemaHallId()).isEmpty())
            throw new CinemaHallNotFoundException();
    }


}
