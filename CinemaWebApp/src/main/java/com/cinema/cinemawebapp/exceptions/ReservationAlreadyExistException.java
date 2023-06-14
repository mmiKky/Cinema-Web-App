package com.cinema.cinemawebapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ReservationAlreadyExistException extends Exception{

    public ReservationAlreadyExistException(){
        super("Reservation for this seat already exists");
    }

    public ReservationAlreadyExistException(String msg){
        super(msg);
    }
}
