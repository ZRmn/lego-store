package com.zrmn.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SignInException extends RuntimeException
{
    public SignInException(String message)
    {
        super(message);
    }
}
