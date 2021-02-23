package com.codecool.userservice.controller;

import com.codecool.userservice.model.Trader;
import com.codecool.userservice.repository.UserRepository;
import com.codecool.userservice.service.PasswordEncrypter;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
//@CrossOrigin(methods = {GET, POST, PUT, DELETE}, origins = "http://localhost:3000")
//@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncrypter passwordEncrypter;

    @GetMapping("getuser/{username}")
    public Trader getUser(@PathVariable("username") String username) {
        return userRepository.findByUsername(username);
    }

    @PostMapping("registeruser")
    public boolean registerUser(@RequestBody Trader trader) {
        List<String> role = Arrays.asList("ROLE_USER");
        Trader encyptedTrader = Trader.builder()
                .username(trader.getUsername())
                .password(passwordEncrypter.encodePassword(trader.getPassword()))
                .roles(role)
                .build();

        userRepository.save(encyptedTrader);
        return true;
    }
}
