package com.zrmn.web.security.config;

import com.zrmn.web.security.token.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
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
                .disable();
    }
}
