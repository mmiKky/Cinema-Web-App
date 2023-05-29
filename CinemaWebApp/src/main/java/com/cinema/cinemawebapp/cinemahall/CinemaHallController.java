package com.cinema.cinemawebapp.cinemahall;

import com.cinema.cinemawebapp.cinemahall.models.CinemaHall;
import com.cinema.cinemawebapp.cinemahall.models.CinemaHallResponse;
import com.cinema.cinemawebapp.cinemahall.models.Seat;
import com.cinema.cinemawebapp.cinemahall.utils.CinemaHallFileParser;
import com.cinema.cinemawebapp.exceptions.CinemaHallNotFoundException;
import com.cinema.cinemawebapp.exceptions.CinemaHallStructureFileNotAvailableException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/cinema-hall")
public class CinemaHallController {
    private final CinemaHallRepository cinemaHallRepository;
    private final CinemaHallFileParser fileParser;

    public CinemaHallController(CinemaHallRepository cinemaHallRepository, CinemaHallFileParser fileParser){
        this.cinemaHallRepository = cinemaHallRepository;
        this.fileParser = fileParser;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CinemaHallResponse> getCinemaHall(@PathVariable int id) throws CinemaHallNotFoundException, CinemaHallStructureFileNotAvailableException {
        CinemaHall cinemaHall = cinemaHallRepository.findById(id).orElseThrow(CinemaHallNotFoundException::new);
        CinemaHallResponse cinemaHallResponse = createCinemaHallResponse(cinemaHall);

        return ResponseEntity.ok(cinemaHallResponse);
    }

    @GetMapping
    public ResponseEntity<Iterable<CinemaHallResponse>> getAllCinemaHalls() throws CinemaHallStructureFileNotAvailableException {
        List<CinemaHallResponse> cinemaHallResponseList = new ArrayList<>();

        for(var cinemaHall: cinemaHallRepository.findAll()){
            CinemaHallResponse cinemaHallResponse = createCinemaHallResponse(cinemaHall);
            cinemaHallResponseList.add(cinemaHallResponse);
        }

        return ResponseEntity.ok(cinemaHallResponseList);
    }

    private CinemaHallResponse createCinemaHallResponse(CinemaHall cinemaHall) throws CinemaHallStructureFileNotAvailableException {
        String filePath = Objects.requireNonNull(
                getClass().getClassLoader().getResource(
                        "SeatsConfigurationFiles/" + cinemaHall.getSeatsFileName())).getPath();
        List<Seat> seatsList = fileParser.parseFile(filePath);

        if(seatsList.isEmpty())
            throw new CinemaHallStructureFileNotAvailableException();

        CinemaHallResponse cinemaHallResponse = new CinemaHallResponse();
        cinemaHallResponse.setId(cinemaHall.getId());
        cinemaHallResponse.setName(cinemaHall.getName());
        cinemaHallResponse.setSeatsList(seatsList);

        return cinemaHallResponse;
    }
}
