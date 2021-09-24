package com.discountengine.demo.exception;

import lombok.Getter;

@Getter
public class InvalidInputException extends RuntimeException{

    private final String message;

    public InvalidInputException(String message){
        super(message);
        this.message = message;
    }
}
