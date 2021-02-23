package com.codecool.security.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/signin").permitAll() // allowed by anyone
                .antMatchers("/stock/getallstocks").permitAll() // allowed by anyone
                .antMatchers( "/user/**").authenticated() // allowed only when signed in
                .antMatchers( "/stock/**").authenticated() // allowed only when signed in
                //.antMatchers(HttpMethod.DELETE, "/vehicles/**").hasRole("ADMIN") // allowed if signed in with ADMIN role
                .anyRequest().denyAll(); // anything else is denied
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

