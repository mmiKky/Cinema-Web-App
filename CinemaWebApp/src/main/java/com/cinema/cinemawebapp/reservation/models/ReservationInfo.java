package com.cinema.cinemawebapp.reservation.models;

import lombok.Data;

@Data
public class ReservationInfo {
    private Reservation reservation;
    private String userName;
    private String userSurname;
    private String userEmail;
    private String screeningDate;
    private String screeningStartTime;
    private String screeningEndTime;
    private int movieId;
    private String movieTitle;
    private String CinemaHallName;
}
