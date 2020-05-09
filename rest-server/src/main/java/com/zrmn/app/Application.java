package com.zrmn.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableJpaRepositories(basePackages = "com.zrmn.model.repositories")
@EntityScan(basePackages = "com.zrmn.model.entities")
@EnableWebSecurity
@ComponentScan(basePackages = {"com.zrmn", "com.zrmn.model.services"})
//@EnableWebMvc
@SpringBootApplication
public class Application
{
    public static void main(String[] args)
    {
        SpringApplication.run(Application.class);
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer()
//    {
//        return new WebMvcConfigurer()
//        {
//            @Value("${upload.path}")
//            private String uploadPath;
//
//            @Override
//            public void addResourceHandlers(ResourceHandlerRegistry registry)
//            {
//                registry
//                        .addResourceHandler("/resources/**")
//                        .addResourceLocations("file:" + uploadPath);
//            }
//        };
//    }
}