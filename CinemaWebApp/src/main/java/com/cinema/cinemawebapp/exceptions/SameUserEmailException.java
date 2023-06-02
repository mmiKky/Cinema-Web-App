package com.cinema.cinemawebapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SameUserEmailException extends Exception{

    public SameUserEmailException(){
        super("User with this email already exists");
    }

    public SameUserEmailException(String msg){
        super(msg);
    }
}
