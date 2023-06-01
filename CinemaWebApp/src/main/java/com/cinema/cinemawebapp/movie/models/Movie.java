package com.cinema.cinemawebapp.movie.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private String genre;
    private int runtimeMinutes;
}
