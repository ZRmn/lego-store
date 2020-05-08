package com.zrmn.model.services;

import com.zrmn.model.entities.Token;
import com.zrmn.model.exceptions.SignInException;

public interface SignInService
{
    Token signIn(String login, String password) throws SignInException;
}
