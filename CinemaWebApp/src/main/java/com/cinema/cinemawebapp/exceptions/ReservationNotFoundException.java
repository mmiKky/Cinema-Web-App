package com.cinema.cinemawebapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReservationNotFoundException extends Exception{

    public ReservationNotFoundException(){
        super();
    }

    public ReservationNotFoundException(String msg){
        super(msg);
    }
}
