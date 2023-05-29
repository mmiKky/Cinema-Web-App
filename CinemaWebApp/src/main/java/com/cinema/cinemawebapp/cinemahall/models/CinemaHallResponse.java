package com.cinema.cinemawebapp.cinemahall.models;

import lombok.Data;

import java.util.List;

@Data
public class CinemaHallResponse {
    private int id;
    private String name;
    private List<Seat> seatsList;
}
