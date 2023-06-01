package com.cinema.cinemawebapp.screening;

import com.cinema.cinemawebapp.exceptions.ScreeningNotFoundException;
import com.cinema.cinemawebapp.screening.models.Screening;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/screenings")
public class ScreeningController {

    private final ScreeningRepository screeningRepository;

    public ScreeningController(ScreeningRepository screeningRepository) {
        this.screeningRepository = screeningRepository;
    }

    @GetMapping
    public ResponseEntity<Iterable<Screening>> getAllScreenings(){
        return ResponseEntity.ok(screeningRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Screening> getScreening(@PathVariable int id) throws ScreeningNotFoundException {
        Screening foundScreening = screeningRepository.findById(id).orElseThrow(ScreeningNotFoundException::new);
        return ResponseEntity.ok(foundScreening);
    }

    @PostMapping
    public ResponseEntity<Screening> addScreening(@RequestBody Screening newScreening){
        return ResponseEntity.ok(screeningRepository.save(newScreening));
    }

    @PutMapping
    public ResponseEntity<Screening> updateScreening(@RequestBody Screening updatedScreening) throws ScreeningNotFoundException {
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
}
