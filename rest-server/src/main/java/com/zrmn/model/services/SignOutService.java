package com.zrmn.model.services;

import com.zrmn.model.exceptions.NotFoundException;

public interface SignOutService
{
    void signOut(String token) throws NotFoundException;
}
