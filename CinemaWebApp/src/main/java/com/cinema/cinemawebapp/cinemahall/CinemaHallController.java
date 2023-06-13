package com.cinema.cinemawebapp.cinemahall;

import com.cinema.cinemawebapp.cinemahall.models.CinemaHall;
import com.cinema.cinemawebapp.cinemahall.models.CinemaHallResponse;
import com.cinema.cinemawebapp.cinemahall.models.Seat;
import com.cinema.cinemawebapp.cinemahall.models.SeatDTO;
import com.cinema.cinemawebapp.cinemahall.utils.CinemaHallFileParser;
import com.cinema.cinemawebapp.exceptions.CinemaHallNotFoundException;
import com.cinema.cinemawebapp.exceptions.CinemaHallStructureFileNotAvailableException;
import com.cinema.cinemawebapp.exceptions.ScreeningNotFoundException;
import com.cinema.cinemawebapp.reservation.ReservationRepository;
import com.cinema.cinemawebapp.reservation.models.Reservation;
import com.cinema.cinemawebapp.screening.ScreeningRepository;
import com.cinema.cinemawebapp.screening.models.Screening;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cinema-hall")
public class CinemaHallController {
    private final CinemaHallRepository cinemaHallRepository;
    private final CinemaHallFileParser fileParser;

    private final ReservationRepository reservationRepository;

    private final ScreeningRepository screeningRepository;

    public CinemaHallController(CinemaHallRepository cinemaHallRepository, CinemaHallFileParser fileParser, ReservationRepository reservationRepository, ScreeningRepository screeningRepository){
        this.cinemaHallRepository = cinemaHallRepository;
        this.fileParser = fileParser;
        this.reservationRepository = reservationRepository;
        this.screeningRepository = screeningRepository;
    }

    @GetMapping("/{screeningId}")
    public ResponseEntity<CinemaHallResponse> getCinemaHall(@PathVariable int screeningId) throws CinemaHallNotFoundException, CinemaHallStructureFileNotAvailableException, ScreeningNotFoundException {
        Screening screening = screeningRepository.findById(screeningId).orElseThrow(ScreeningNotFoundException::new);
        CinemaHall cinemaHall = cinemaHallRepository.findById(screening.getCinemaHallId()).orElseThrow(CinemaHallNotFoundException::new);
        CinemaHallResponse cinemaHallResponse = createCinemaHallResponse(cinemaHall);
        cinemaHallResponse.setSeatsList(updateAvailability(cinemaHallResponse.getSeatsList(), screening.getId()));
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

    private List<Seat> updateAvailability(List<Seat> seats, int screeningId){
        List<Reservation> reservationList = reservationRepository.findReservationsByScreeningId(screeningId);
        for(Seat seat : seats){
            seat.setAvailable(reservationList.stream().noneMatch(r ->
                    r.getSeatNr() == seat.getSeatNr() && r.getSeatRow() == seat.getRow()
            ));
        }
        return seats;
    }
}
