package com.zrmn.model.services;

import com.zrmn.model.entities.Authority;
import com.zrmn.model.entities.User;
import com.zrmn.model.exceptions.NotFoundException;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UsersService
{
    List<User> getAll();

    User get(Long id) throws NotFoundException;

    void save(User user);

    void delete(Long id) throws NotFoundException;

    void deleteAll();

    void update(User user) throws NotFoundException;

    User getFromAuthentication(Authentication authentication);

    User getByToken(String token) throws NotFoundException;

    void setState(Long id, User.State state);

    void setAuthorities(Long id, List<Authority> authorities);
}
