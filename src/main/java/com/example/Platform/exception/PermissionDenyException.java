package com.example.Platform.exception;

public class PermissionDenyException extends RuntimeException{
    public PermissionDenyException(String message) {
        super(message);
    }
}
