package com.codecool.userservice.controller;

import com.codecool.userservice.model.Trader;
import com.codecool.userservice.repository.UserRepository;
import com.codecool.userservice.service.PasswordEncrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
//@CrossOrigin(methods = {GET, POST, PUT, DELETE}, origins = "http://localhost:3000")
//@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncrypter passwordEncrypter;

    @GetMapping("getuser/{id}")
    public Trader getUser(@PathVariable("id") long id) {
        return userRepository.findById(id);
    }

    @PostMapping("saveuser")
    public boolean saveUser(@RequestBody Trader trader) {
        Trader encyptedTrader = Trader.builder()
                .username(trader.getUsername())
                .password(passwordEncrypter.encodePassword(trader.getPassword()))
                .build();

        userRepository.save(encyptedTrader);
        return true;
    }
}
