package com.codecool.userservice.service_caller;

import com.codecool.userservice.model.UserAccountRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserAccountCaller {

    @Autowired
    private RestTemplate template;

    private final String registerUserAccountURL = "http://useraccount/user/registeruseraccount/";


    public boolean registerUserAccount(UserAccountRegistration userAccountRegistration){

        return template.postForObject(registerUserAccountURL, userAccountRegistration, Boolean.class);
    }

}
