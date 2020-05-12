package com.zrmn.web.security.config;

import com.zrmn.model.entities.Authority;
import com.zrmn.model.entities.User;
import com.zrmn.model.repositories.UsersRepository;
import com.zrmn.web.security.token.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private UsersRepository usersRepository;

    @Value("${admin.login}")
    private String adminLogin;

    @Value("${admin.password}")
    private String adminPassword;

    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createDefaultAdmin()
    {
        if(!usersRepository.getByLogin(adminLogin).isPresent())
        {
            List<Authority> authorities = Collections.singletonList(Authority.builder().role(User.Role.ADMIN).build());
            User user = User.builder()
                    .login(adminLogin)
                    .password(passwordEncoder().encode(adminPassword))
                    .authorities(authorities)
                    .state(User.State.ACTIVE)
                    .build();

            usersRepository.save(user);
        }
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter()
    {
        return new TokenAuthenticationFilter();
    }

    @Bean
    public FilterRegistrationBean filterRegistration(TokenAuthenticationFilter tokenAuthenticationFilter)
    {
        FilterRegistrationBean registration = new FilterRegistrationBean(tokenAuthenticationFilter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
                .addFilterBefore(tokenAuthenticationFilter(), BasicAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider)
                .exceptionHandling()
                .authenticationEntryPoint((httpServletRequest, httpServletResponse, e) -> {
                    httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                })
                    .and()
                .authorizeRequests()
                .antMatchers("/sign-out").authenticated()
                .antMatchers("/sign-in", "/sign-up").anonymous()
                .antMatchers("/cart/**", "/orders/**").hasAuthority("CUSTOMER")
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/**").permitAll()
                    .and()
                .csrf()
                .disable()
                .cors();
    }
}
