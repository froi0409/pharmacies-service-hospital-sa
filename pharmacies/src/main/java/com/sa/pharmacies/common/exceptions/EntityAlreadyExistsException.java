package com.sa.pharmacies.common.exceptions;

public class EntityAlreadyExistsException extends Exception{
    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}