package com.cinema.cinemawebapp.reservation;

import com.cinema.cinemawebapp.reservation.models.Reservation;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {
}
