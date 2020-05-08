package com.zrmn.web.security.token;

import com.zrmn.model.entities.User;
import com.zrmn.model.exceptions.NotFoundException;
import com.zrmn.model.services.UsersService;
import com.zrmn.web.security.userdetails.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthenticationProvider implements AuthenticationProvider
{
    @Autowired
    private UsersService usersService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
        User user;

        try
        {
            user = usersService.getByToken(tokenAuthentication.getName());
        }
        catch (NotFoundException e)
        {
            throw new AuthenticationException(e.getMessage()) {};
        }

        UserDetails userDetails = new UserDetailsImpl(user);
        tokenAuthentication.setPrincipal(userDetails);
        tokenAuthentication.setAuthenticated(true);

        return tokenAuthentication;
    }

    @Override
    public boolean supports(Class<?> cls)
    {
        return TokenAuthentication.class.equals(cls);
    }
}
