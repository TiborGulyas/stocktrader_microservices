package com.codecool.security.service_caller;

import com.codecool.security.model.internal.PlaceOffer;
import com.codecool.security.model.internal.ReplaceOffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserAccountCaller {

    @Autowired
    private RestTemplate template;

    private final String registerUserAccountURL = "http://useraccount/user/";


    public String placeOffer(PlaceOffer placeOffer){

        return template.postForObject(registerUserAccountURL+"placeoffer", placeOffer, String.class);
    }

    public String replaceOffer(ReplaceOffer replaceOffer) {
        return template.postForObject(registerUserAccountURL+"replaceoffer", replaceOffer, String.class);
    }
}
