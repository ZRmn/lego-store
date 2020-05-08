package com.zrmn.model.services;

import com.zrmn.model.entities.Authority;
import com.zrmn.model.entities.Customer;
import com.zrmn.model.entities.User;
import com.zrmn.model.exceptions.SignUpException;
import com.zrmn.model.forms.SignUpForm;
import com.zrmn.model.repositories.CustomersRepository;
import com.zrmn.model.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SignUpServiceImpl implements SignUpService
{
    @Autowired
    private CustomersRepository customersRepository;
    
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private void throwIfPresent(String login) throws SignUpException
    {
        if(usersRepository.getByLogin(login).isPresent())
        {
            throw new SignUpException("User with that login is already registered");
        }
    }

    @Override
    public void signUpUser(SignUpForm signUpForm) throws SignUpException
    {
        throwIfPresent(signUpForm.getLogin());

        List<Authority> authorities = Collections.singletonList(Authority.builder().role(User.Role.CUSTOMER).build());

        Customer customer = Customer.builder()
                .login(signUpForm.getLogin())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .authorities(authorities)
                .state(User.State.ACTIVE)
                .fullName(signUpForm.getFullName())
                .phoneNumber(signUpForm.getPhoneNumber())
                .build();

        customersRepository.save(customer);
    }
    
    @Override
    public void signUpAdmin(String login, String password) throws SignUpException
    {
        throwIfPresent(login);

        List<Authority> authorities = Collections.singletonList(Authority.builder().role(User.Role.ADMIN).build());

        User user = User.builder()
                .login(login)
                .password(passwordEncoder.encode(password))
                .authorities(authorities)
                .state(User.State.ACTIVE)
                .build();
                
        usersRepository.save(user);
    }
}
