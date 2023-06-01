package com.cinema.cinemawebapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CinemaHallStructureFileNotAvailableException extends Exception{
    public CinemaHallStructureFileNotAvailableException(){
        super();
    }

    public CinemaHallStructureFileNotAvailableException(String msg){
        super(msg);
    }
}
