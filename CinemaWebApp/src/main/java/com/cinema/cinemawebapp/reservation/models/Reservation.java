package com.cinema.cinemawebapp.reservation.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int userId;
    private int screeningId;
    private int seatRow;
    private int seatNr;
    private double price;
}
