package com.cinema.cinemawebapp.screening;

import com.cinema.cinemawebapp.screening.models.Screening;
import org.springframework.data.repository.CrudRepository;

public interface ScreeningRepository extends CrudRepository<Screening, Integer> {
}
