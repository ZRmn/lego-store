package com.zrmn.web.controllers;

import com.zrmn.model.forms.SignUpForm;
import com.zrmn.model.services.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sign-up")
public class SignUpController
{
    @Autowired
    private SignUpService signUpService;

    @PostMapping
    public ResponseEntity signUp(@RequestBody SignUpForm signUpForm)
    {
        signUpService.signUpUser(signUpForm);
        return ResponseEntity.ok("Signed up");
    }
}
