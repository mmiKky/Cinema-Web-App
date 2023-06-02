package com.cinema.cinemawebapp.reservation;

import com.cinema.cinemawebapp.reservation.models.Reservation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private ReservationRepository reservationRepository;

    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @PostMapping()
    public ResponseEntity<Reservation> addReservation(@RequestBody Reservation reservation){
        return ResponseEntity.ok(reservationRepository.save(reservation));
    }

//    @GetMapping("/user/{userId}")
//    public ResponseEntity<Iterable<ReservationInfo>> getAllReservationsByUserId(@PathVariable int userId) throws ReservationNotFoundException {
//        List<ReservationInfo> reservationInfos = reservationRepository.findReservationInfoByUserId(userId);
//
//        if (!reservationInfos.isEmpty())
//            return ResponseEntity.ok(reservationInfos);
//        else
//            throw new ReservationNotFoundException();
//    }
//
//    @GetMapping("/hall/{hallId}")
//    public ResponseEntity<Iterable<Tuple>> getAllReservedSeatsByHallId(@PathVariable int hallId){
//        return ResponseEntity.ok(reservationRepository.findReservedSeatsByHallId(hallId));
//    }
}
