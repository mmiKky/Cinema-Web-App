package com.cinema.cinemawebapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ScreeningNotFoundException extends Exception{

    public ScreeningNotFoundException(){
        super();
    }

    public ScreeningNotFoundException(String msg){
        super(msg);
    }
}
