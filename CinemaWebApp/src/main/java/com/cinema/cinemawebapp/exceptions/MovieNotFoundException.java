package com.cinema.cinemawebapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MovieNotFoundException extends Exception{

    public MovieNotFoundException(){}

    public MovieNotFoundException(String msg){
        super(msg);
    }
}
