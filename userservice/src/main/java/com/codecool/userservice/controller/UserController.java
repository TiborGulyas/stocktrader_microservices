package com.codecool.userservice.controller;

import com.codecool.userservice.model.Trader;
import com.codecool.userservice.model.TraderRegistrationForm;
import com.codecool.userservice.model.UserAccountRegistration;
import com.codecool.userservice.repository.UserRepository;
import com.codecool.userservice.service.PasswordEncrypter;
import com.codecool.userservice.service_caller.UserAccountCaller;
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

    @Autowired
    private UserAccountCaller userAccountCaller;

    @GetMapping("gettrader/{username}")
    public Trader getUser(@PathVariable("username") String username) {
        return userRepository.findByUsername(username);
    }

    @PostMapping("registertrader")
    public boolean registerTrader(@RequestBody TraderRegistrationForm traderRegistrationForm) {
        System.out.println("---THIS IS THE TRADER REGISTRATION FORM:");
        System.out.println(traderRegistrationForm);

        List<String> role = Arrays.asList("ROLE_USER");

        Trader encyptedTrader = Trader.builder()
                .username(traderRegistrationForm.getUsername())
                .password(passwordEncrypter.encodePassword(traderRegistrationForm.getPassword()))
                .roles(role)
                .build();

        UserAccountRegistration userAccountRegistration = UserAccountRegistration.builder()
                .username(traderRegistrationForm.getUsername())
                .nickName(traderRegistrationForm.getNickName())
                .profilePic_(traderRegistrationForm.getProfilePic_())
                .eMail_(traderRegistrationForm.getE_mail())
                .build();


        if (userAccountCaller.registerUserAccount(userAccountRegistration)){
            userRepository.save(encyptedTrader);
            return true;
        }
        return false;
    }

    @PostMapping("registeruser")
    public void registerUser(@RequestBody Trader trader){
        List<String> role = Arrays.asList("ROLE_USER");

        Trader encyptedTrader = Trader.builder()
                .username(trader.getUsername())
                .password(passwordEncrypter.encodePassword(trader.getPassword()))
                .roles(role)
                .build();

        userRepository.save(encyptedTrader);
    }
}
