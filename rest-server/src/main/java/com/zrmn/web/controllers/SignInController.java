package com.zrmn.web.controllers;

import com.zrmn.model.entities.Token;
import com.zrmn.model.forms.SignInForm;
import com.zrmn.model.services.SignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sign-in")
public class SignInController
{
    @Autowired
    private SignInService signInService;

    @PostMapping
    public ResponseEntity signIn(@RequestBody SignInForm signInForm)
    {
        Token token = signInService.signIn(signInForm.getLogin(), signInForm.getPassword());
        return ResponseEntity.ok(token);
    }
}
