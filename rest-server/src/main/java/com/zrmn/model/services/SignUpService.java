package com.zrmn.model.services;

import com.zrmn.model.exceptions.SignUpException;
import com.zrmn.model.forms.SignUpForm;

public interface SignUpService
{
    void signUpUser(SignUpForm signUpForm) throws SignUpException;
    void signUpAdmin(String login, String password) throws SignUpException;
}
