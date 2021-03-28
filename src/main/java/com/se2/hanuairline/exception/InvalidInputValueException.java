package com.se2.hanuairline.exception;

public class InvalidInputValueException extends Exception{
    InvalidInputValueException(){
        super();
    }
    public InvalidInputValueException(String message){
        super(message);
    }
}
