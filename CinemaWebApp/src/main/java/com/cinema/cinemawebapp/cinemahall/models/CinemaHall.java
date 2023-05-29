package com.cinema.cinemawebapp.cinemahall.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "CinemaHall")
public class CinemaHall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String seatsFileName;
}
