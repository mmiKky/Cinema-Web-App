package com.cinema.cinemawebapp.screening.models;

import com.cinema.cinemawebapp.cinemahall.models.CinemaHall;
import com.cinema.cinemawebapp.movie.models.Movie;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class ScreeningResponse {
    private int id;
    private Movie movie;
    private CinemaHall cinemaHall;
    private String date;
    private String startTime;
    private String endTime;
}