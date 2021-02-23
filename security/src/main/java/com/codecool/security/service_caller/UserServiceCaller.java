package com.codecool.security.service_caller;


import com.codecool.security.model.Trader;
import com.codecool.security.model.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceCaller {

    @Autowired
    private RestTemplate template;

    private final String getTraderURL = "http://userservice/getuser/";
    private final String registerUserURL = "http://userservice/registeruser/";

    public Trader getTrader(String username){

        return template.getForObject(getTraderURL + username, Trader.class);
    }

    public Boolean registerTrader(UserCredentials userCredentials){
        return template.postForObject(registerUserURL, userCredentials, Boolean.class);
    }
}
