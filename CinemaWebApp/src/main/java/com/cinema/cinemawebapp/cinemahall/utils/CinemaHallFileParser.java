package com.cinema.cinemawebapp.cinemahall.utils;

import com.cinema.cinemawebapp.cinemahall.models.Seat;
import com.cinema.cinemawebapp.exceptions.CinemaHallStructureFileNotAvailableException;

import java.util.List;


public interface CinemaHallFileParser {
    List<Seat> parseFile(String filePath) throws CinemaHallStructureFileNotAvailableException;
}
