package com.zrmn.model.services;

import com.zrmn.model.entities.Token;
import com.zrmn.model.entities.User;
import com.zrmn.model.exceptions.SignInException;
import com.zrmn.model.repositories.TokensRepository;
import com.zrmn.model.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SignInServiceImpl implements SignInService
{
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TokensRepository tokensRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Token signIn(String login, String password) throws SignInException
    {
        User user = usersRepository.getByLogin(login)
                .orElseThrow(() -> new SignInException("Login or password is incorrect"));

        if(!passwordEncoder.matches(password, user.getPassword()))
        {
            throw new SignInException("Login or password is incorrect");
        }

        Token token = Token.builder()
                .value(UUID.randomUUID().toString())
                .build();

        tokensRepository.save(token);
        user.getTokens().add(token);
        usersRepository.save(user);

        return token;
    }
}
