package com.zrmn.model.services;

import com.zrmn.model.entities.Token;
import com.zrmn.model.exceptions.NotFoundException;
import com.zrmn.model.repositories.TokensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignOutServiceImpl implements SignOutService
{
    @Autowired
    private TokensRepository tokensRepository;

    @Override
    public void signOut(String token) throws NotFoundException
    {
        Token tkn = tokensRepository.getByValue(
                token).orElseThrow(() -> new NotFoundException("No such token"));

        tokensRepository.delete(tkn);
    }
}
