package com.zrmn.web.security.token;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class TokenAuthentication implements Authentication
{
    private String token;
    private boolean isAuthenticated;
    private UserDetails userDetails;

    public TokenAuthentication(String token)
    {
        this.token = token;
    }

    public void setPrincipal(UserDetails userDetails)
    {
        this.userDetails = userDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return userDetails.getAuthorities();
    }

    @Override
    public Object getCredentials()
    {
        return token;
    }

    @Override
    public Object getDetails()
    {
        return null;
    }

    @Override
    public Object getPrincipal()
    {
        return userDetails;
    }

    @Override
    public boolean isAuthenticated()
    {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException
    {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName()
    {
        return token;
    }
}
