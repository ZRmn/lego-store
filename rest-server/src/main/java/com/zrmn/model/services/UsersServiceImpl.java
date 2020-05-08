package com.zrmn.model.services;

import com.zrmn.model.entities.Authority;
import com.zrmn.model.entities.User;
import com.zrmn.model.exceptions.NotFoundException;
import com.zrmn.model.repositories.UsersRepository;
import com.zrmn.web.security.userdetails.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersServiceImpl implements UsersService
{
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public List<User> getAll()
    {
        return usersRepository.findAll();
    }

    @Override
    public User get(Long id) throws NotFoundException
    {
        return usersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No such user"));
    }

    @Override
    public void save(User user)
    {
        usersRepository.save(user);
    }

    @Override
    public void delete(Long id) throws NotFoundException
    {
        usersRepository.delete(get(id));
    }

    @Override
    public void deleteAll()
    {
        usersRepository.deleteAll();
    }

    @Override
    public void update(User user) throws NotFoundException
    {
        get(user.getId());
        usersRepository.save(user);
    }

    @Override
    public User getFromAuthentication(Authentication authentication)
    {
        return ((UserDetailsImpl) authentication.getPrincipal()).getUser();
    }

    @Override
    public User getByToken(String token) throws NotFoundException
    {
        return usersRepository.findByTokens_Value(token)
                .orElseThrow(() -> new NotFoundException("No such token"));
    }

    @Override
    public void setState(Long id, User.State state)
    {
        User user = get(id);
        user.setState(state);
    }

    @Override
    public void setAuthorities(Long id, List<Authority> authorities)
    {
        User user = get(id);
        user.setAuthorities(authorities);
    }
}
