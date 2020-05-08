package com.zrmn.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SignUpException extends RuntimeException
{
    public SignUpException(String message)
    {
        super(message);
    }
}
