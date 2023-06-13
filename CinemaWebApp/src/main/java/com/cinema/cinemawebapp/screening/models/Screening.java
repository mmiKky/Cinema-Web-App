package com.cinema.cinemawebapp.screening.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Screening")
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int movieId;
    private int cinemaHallId;
    private String date;
    private String startTime;
    private String endTime;
}