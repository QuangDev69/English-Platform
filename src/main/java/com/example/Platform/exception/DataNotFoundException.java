package com.example.Platform.exception;

public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException (String message) {
        super(message);
    }
}
