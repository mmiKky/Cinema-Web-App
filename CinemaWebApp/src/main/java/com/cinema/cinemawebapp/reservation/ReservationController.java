package com.cinema.cinemawebapp.reservation;

import com.cinema.cinemawebapp.cinemahall.CinemaHallRepository;
import com.cinema.cinemawebapp.cinemahall.models.CinemaHall;
import com.cinema.cinemawebapp.cinemahall.models.SeatDTO;
import com.cinema.cinemawebapp.exceptions.*;
import com.cinema.cinemawebapp.movie.MovieRepository;
import com.cinema.cinemawebapp.movie.models.Movie;
import com.cinema.cinemawebapp.reservation.models.Reservation;
import com.cinema.cinemawebapp.reservation.models.ReservationInfo;
import com.cinema.cinemawebapp.reservation.models.Seat;
import com.cinema.cinemawebapp.screening.ScreeningRepository;
import com.cinema.cinemawebapp.screening.models.Screening;
import com.cinema.cinemawebapp.user.UserRepository;
import com.cinema.cinemawebapp.user.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final CinemaHallRepository cinemaHallRepository;

    public ReservationController(ReservationRepository reservationRepository, UserRepository userRepository,
                                 ScreeningRepository screeningRepository, MovieRepository movieRepository,
                                 CinemaHallRepository cinemaHallRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.cinemaHallRepository = cinemaHallRepository;
    }

    @PostMapping()
    public ResponseEntity<Reservation> addReservation(@RequestBody Reservation reservation) {
        return ResponseEntity.ok(reservationRepository.save(reservation));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Iterable<ReservationInfo>> getAllReservationsByUserId(@PathVariable int userId)
            throws UserNotFoundException, ScreeningNotFoundException, MovieNotFoundException, CinemaHallNotFoundException {

        List<ReservationInfo> usersReservationInfos = new ArrayList<>();
        User user;
        Screening screening;
        Movie movie;
        CinemaHall cinemaHall;

        for (Reservation reservation : reservationRepository.findAll()) {
            if (reservation.getUserId() != userId)
                continue;

            user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
            screening = screeningRepository.findById(reservation.getScreeningId()).orElseThrow(ScreeningNotFoundException::new);
            movie = movieRepository.findById(screening.getMovieId()).orElseThrow(MovieNotFoundException::new);
            cinemaHall = cinemaHallRepository.findById(screening.getCinemaHallId()).orElseThrow(CinemaHallNotFoundException::new);

            Optional<ReservationInfo> ri = usersReservationInfos.stream().filter(
                    reservationInfo -> reservationInfo.getReservation().getUserId() == reservation.getUserId() &&
                            reservationInfo.getReservation().getScreeningId() == reservation.getScreeningId()).findFirst();


            Seat s = new Seat();
            s.setSeatNr(reservation.getSeatNr());
            s.setRow(reservation.getSeatRow());
            s.setPrice(reservation.getPrice());


            if (ri.isEmpty()) {
                ReservationInfo reservationInfo = new ReservationInfo();
                reservationInfo.setReservation(reservation);
                reservationInfo.setUserName(user.getName());
                reservationInfo.setUserSurname(user.getSurname());
                reservationInfo.setUserEmail(user.getEmail());
                reservationInfo.setScreeningDate(screening.getDate());
                reservationInfo.setScreeningStartTime(screening.getStartTime());
                reservationInfo.setScreeningEndTime(screening.getEndTime());
                reservationInfo.setMovieId(movie.getId());
                reservationInfo.setMovieTitle(movie.getTitle());
                reservationInfo.setCinemaHallName(cinemaHall.getName());
                ArrayList<Seat> seatArrayList = new ArrayList<>();
                seatArrayList.add(s);
                reservationInfo.setSeats(seatArrayList);
                usersReservationInfos.add(reservationInfo);
            } else {
                ri.get().getSeats().add(s);
            }
        }

        return ResponseEntity.ok(usersReservationInfos);
    }

    @GetMapping("/screening/{screeningId}")
    public ResponseEntity<Iterable<Seat>> getAllReservedSeatsByScreeningId(@PathVariable int screeningId) {
        List<Seat> reservedSeatsList = new ArrayList<>();
        Optional<Screening> screening;

        for (Reservation reservation : reservationRepository.findReservationsByScreeningId(screeningId)) {
            screening = screeningRepository.findById(reservation.getScreeningId());

            if (screening.isEmpty()) // just return empty list
                break;


            var reservedSeat = new Seat();
            reservedSeat.setRow(reservation.getSeatRow());
            reservedSeat.setSeatNr(reservation.getSeatNr());
            reservedSeatsList.add(reservedSeat);
        }

        return ResponseEntity.ok(reservedSeatsList);
    }
}
