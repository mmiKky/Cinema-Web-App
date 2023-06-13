package com.cinema.cinemawebapp.cinemahall.models;

import lombok.Data;
@Data
public class SeatDTO {
        private int row;
        private int seatNr;
        private boolean isDouble;
        private double price;
        private boolean available;
}
