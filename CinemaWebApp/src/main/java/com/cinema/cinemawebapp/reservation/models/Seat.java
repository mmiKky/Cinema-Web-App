package com.cinema.cinemawebapp.reservation.models;

import lombok.Data;

@Data
public class Seat {
    private int row;
    private int seatNr;
    private double price;
    private boolean available;
}
