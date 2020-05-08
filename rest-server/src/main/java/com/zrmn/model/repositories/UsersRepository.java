package com.zrmn.model.repositories;

import com.zrmn.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long>
{
    Optional<User> getByLogin(String login);
    Optional<User> findByTokens_Value(String value);
}