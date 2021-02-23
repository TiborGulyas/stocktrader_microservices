package com.codecool.security.controller;


import com.codecool.security.model.UserCredentials;
import com.codecool.security.security.JwtTokenServices;
import com.codecool.security.service_caller.UserServiceCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenServices jwtTokenServices;

    @Autowired
    UserServiceCaller userServiceCaller;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenServices jwtTokenServices) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenServices = jwtTokenServices;
    }

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody UserCredentials data) {
        System.out.println("---signin running---");
        try {
            String username = data.getUsername();
            // authenticationManager.authenticate calls loadUserByUsername in CustomUserDetailsService
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            List<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String token = jwtTokenServices.createToken(username, roles);

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("roles", roles);
            model.put("token", token);
            System.out.println("---try signin succesfull--");
            return ResponseEntity.ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @GetMapping("/me")
    public String currentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token_username = (String) authentication.getPrincipal();
        return token_username;
    }

    @PostMapping("/register")
    public boolean registerUser(@RequestBody UserCredentials userCredentials){
        return userServiceCaller.registerTrader(userCredentials);
    }


}
