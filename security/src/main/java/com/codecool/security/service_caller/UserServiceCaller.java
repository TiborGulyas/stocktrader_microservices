package com.codecool.security.service_caller;


import com.codecool.security.model.Trader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceCaller {

    @Autowired
    private RestTemplate template;

    private final String getTraderURL = "http://userservice/getuser/";

    public Trader getTrader(String username){

        return template.getForObject(getTraderURL + username, Trader.class);
    }
}
