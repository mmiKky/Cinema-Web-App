package com.cinema.cinemawebapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CinemaHallNotFoundException extends Exception{

    public CinemaHallNotFoundException(){}

    public CinemaHallNotFoundException(String msg){
        super(msg);
    }
}
