package com.zrmn.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableJpaRepositories(basePackages = "com.zrmn.model.repositories")
@EntityScan(basePackages = "com.zrmn.model.entities")
@EnableWebSecurity
@ComponentScan(basePackages = {"com.zrmn", "com.zrmn.model.services"})
@SpringBootApplication
public class Application
{
    public static void main(String[] args)
    {
        SpringApplication.run(Application.class);
    }
}