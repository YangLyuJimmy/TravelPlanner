package com.laioffer.travelplanner.exception;

public class PlanNotExistException extends RuntimeException {
    public PlanNotExistException(String message) {
        super(message);
    }
}
