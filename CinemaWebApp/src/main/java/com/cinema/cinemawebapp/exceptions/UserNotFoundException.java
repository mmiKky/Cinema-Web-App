package com.cinema.cinemawebapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends Exception{

    public UserNotFoundException(){
        super();
    }

    public UserNotFoundException(String msg){
        super(msg);
    }
}
