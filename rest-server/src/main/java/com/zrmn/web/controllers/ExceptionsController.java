package com.zrmn.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsController
{
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity exceptionHandler()
    {
        return ResponseEntity.badRequest().body("Bad request parameters");
    }
}
