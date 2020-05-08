package com.zrmn.web.controllers;

import com.zrmn.model.services.SignOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/sign-out")
public class SignOutController
{
    @Autowired
    private SignOutService signOutService;

    @PostMapping
    public ResponseEntity signOut(Authentication authentication, Map<String, String> params)
    {
        signOutService.signOut(authentication.getName());
        return ResponseEntity.ok("Signed out");
    }
}
