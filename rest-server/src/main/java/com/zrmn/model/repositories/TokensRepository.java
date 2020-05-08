package com.zrmn.model.repositories;

import com.zrmn.model.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokensRepository extends JpaRepository<Token, Long>
{
    Optional<Token> getByValue(String value);
}
