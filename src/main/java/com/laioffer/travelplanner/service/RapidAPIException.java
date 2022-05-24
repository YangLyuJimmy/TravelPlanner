package com.laioffer.travelplanner.service;

public class RapidAPIException extends RuntimeException {
    public RapidAPIException(String errorMessage){
        super(errorMessage);
    }
}
