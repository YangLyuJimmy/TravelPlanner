package com.laioffer.travelplanner.exception;

public class DownloadException extends RuntimeException {
    public DownloadException(String errorMessage){
        super(errorMessage);
    }
}
