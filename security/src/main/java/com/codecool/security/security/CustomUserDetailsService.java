package com.codecool.security.security;

import com.codecool.security.model.Trader;
import com.codecool.security.service_caller.UserServiceCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserServiceCaller userServiceCaller;


    /**
     * Loads the user from the DB and converts it to Spring Security's internal User object.
     * Spring will call this code to retrieve a user upon login from the DB.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Trader trader = userServiceCaller.getTrader(username);
            return new User(trader.getUsername(), trader.getPassword(),
                    trader.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        } catch (Exception e){
            throw (new UsernameNotFoundException("Username: " + username + " not found"));
        }


    }
}
