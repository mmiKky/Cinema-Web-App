package com.cinema.cinemawebapp.reservation;

import com.cinema.cinemawebapp.reservation.models.Reservation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {
    List<Reservation> findReservationsByScreeningId(int screeningId);
}
