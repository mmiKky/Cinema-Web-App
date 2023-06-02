package com.cinema.cinemawebapp.reservation;

import com.cinema.cinemawebapp.reservation.models.Reservation;
import com.cinema.cinemawebapp.reservation.models.ReservationInfo;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {

//    @Query("select Reservation.seatRow, Reservation.seatNr from Reservation " +
//            "join Screening on Reservation.screeningId = Screening.id " +
//            "where Screening.cinemaHallId = :hallId")
//    List<Tuple> findReservedSeatsByHallId(@Param("hallId") int hallId);

    @Query("select Reservation.id, Reservation.userId, Reservation.screeningId, " +
            "Reservation.seatRow, Reservation.seatNr, Reservation.price, User.name, " +
            "User.surname, User.email, Screening.date, Screening.startTime, Screening.endTime, Movie.id, " +
            "Movie.title, CinemaHall.name from Reservation " +
            "join User on User.id = Reservation.id " +
            "join Screening on Screening.id = Reservation.id " +
            "join Movie on Movie.id = Screening.id " +
            "join CinemaHall on CinemaHall.id = Screening.id")
    List<ReservationInfo> findReservationInfoByUserId(@Param("userId") int userId);
}
